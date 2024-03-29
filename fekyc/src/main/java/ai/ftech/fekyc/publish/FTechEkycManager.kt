package ai.ftech.fekyc.publish

import ai.ftech.fekyc.AppConfig
import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.base.extension.setApplication
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.common.onException
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.RegisterEkycData
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.domain.action.FaceMatchingAction
import ai.ftech.fekyc.domain.action.NewSubmitInfoAction
import ai.ftech.fekyc.domain.action.NewUploadPhotoAction
import ai.ftech.fekyc.domain.action.ProcessTransactionAction
import ai.ftech.fekyc.domain.action.RegisterEkycAction
import ai.ftech.fekyc.domain.action.TransactionAction
import ai.ftech.fekyc.domain.model.capture.CaptureData
import ai.ftech.fekyc.domain.model.ekyc.CAPTURE_TYPE
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData
import ai.ftech.fekyc.domain.model.transaction.TransactionProcessData
import ai.ftech.fekyc.infras.EncodeRSA
import ai.ftech.fekyc.presentation.AppPreferences
import ai.ftech.fekyc.presentation.home.HomeActivity
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object FTechEkycManager {
    private var applicationContext: Context? = null

    private val launcherMap = ConcurrentHashMap<Int, ActivityResultLauncher<Unit>?>()
    private var resultLauncher: ActivityResultLauncher<Unit>? = null
    private var pendingCallback: (() -> Unit)? = null
    private var callback: IFTechEkycCallback<FTechEkycInfo>? = null

    private var isActive = true
    private var coolDownTime: Long = -1

    var ftechKey: String = ""
        private set
    var appId: String = ""
        private set
    var transactionId: String = ""
        private set

    private var sessionIdFront: String = ""

    private var sessionIdBack: String = ""

    private var sessionIdFace: String = ""

    @JvmStatic
    fun init(context: Context) {
        applicationContext = context
        setApplication(getApplicationContext())
        AppPreferences.init(context)
    }

    @JvmStatic
    fun getApplicationContext(): Application {
        return applicationContext as? Application
            ?: throw RuntimeException("applicationContext must not null")
    }

    @JvmStatic
    fun register(context: Context) {
        if (context is FragmentActivity) {
            resultLauncher = if (launcherMap.contains(context.hashCode())) {
                launcherMap[context.hashCode()]
            } else {
                context.registerForActivityResult(object :
                    ActivityResultContract<Unit, FTechEkycResult<FTechEkycInfo>>() {
                    override fun createIntent(context: Context, input: Unit?): Intent {
                        return Intent(context, HomeActivity::class.java)
                    }

                    override fun parseResult(
                        resultCode: Int,
                        intent: Intent?
                    ): FTechEkycResult<FTechEkycInfo> {
                        return parseDataFormActivityForResult(resultCode, intent)
                    }
                }) {
                    invokeCallback(callback, it)
                }
            }

            launcherMap[context.hashCode()] = resultLauncher
        }
    }

    @JvmStatic
    fun notifyActive(context: Context) {
        pendingCallback?.let {
            it.invoke()
            pendingCallback = null
        }

        if (context is FragmentActivity) {
            resultLauncher = launcherMap[context.hashCode()]
        }

        isActive = true
    }

    @JvmStatic
    fun notifyInactive(context: Context) {
        isActive = false
    }

    @JvmStatic
    fun unregister(context: Context) {
        callback = null

        if (context is FragmentActivity) {
            launcherMap.remove(context.hashCode())
        }
    }

    @JvmStatic
    fun startEkyc(
        licenseKey: String,
        appId: String,
        callBack: IFTechEkycCallback<FTechEkycInfo>
    ) {
        this.ftechKey = EncodeRSA.encryptData(licenseKey, applicationContext?.packageName)
        this.appId = appId
        checkCoolDownAction {
            if (licenseKey.isEmpty()) {
                throw RuntimeException(getAppString(R.string.empty_license_key))
            }

            if (appId.isEmpty()) {
                throw RuntimeException(getAppString(R.string.empty_license_key))
            }

            if (transactionId.isEmpty()) {
                throw RuntimeException(getAppString(R.string.empty_license_key))
            }

            callback = callBack
            resultLauncher?.launch()
        }
    }

    private fun parseDataFormActivityForResult(
        resultCode: Int,
        intent: Intent?
    ): FTechEkycResult<FTechEkycInfo> {
        val info =
            intent?.getSerializableExtra(HomeActivity.SEND_RESULT_FTECH_EKYC_INFO) as? FTechEkycInfo

        when (resultCode) {
            Activity.RESULT_OK -> {
                return FTechEkycResult<FTechEkycInfo>().apply {
                    this.type = if (info != null) {
                        FTECH_EKYC_RESULT_TYPE.SUCCESS
                    } else {
                        FTECH_EKYC_RESULT_TYPE.ERROR
                    }
                    this.data = info
                }
            }

            Activity.RESULT_CANCELED -> {
                return FTechEkycResult<FTechEkycInfo>().apply {
                    this.type = FTECH_EKYC_RESULT_TYPE.CANCEL
                }
            }

            else -> {
                return FTechEkycResult<FTechEkycInfo>().apply {
                    this.type = FTECH_EKYC_RESULT_TYPE.ERROR
                }
            }
        }
    }

    private fun <T> invokeCallback(callback: IFTechEkycCallback<T>?, result: FTechEkycResult<T>) {
        when (result.type) {
            FTECH_EKYC_RESULT_TYPE.SUCCESS -> {
                if (isActive) {
                    callback?.onSuccess(result.data!!)
                } else {
                    pendingCallback = {
                        callback?.onSuccess(result.data!!)
                    }
                }
            }

            FTECH_EKYC_RESULT_TYPE.ERROR -> {
                if (isActive) {
                    callback?.onFail(result.error)
                } else {
                    pendingCallback = {
                        callback?.onFail(result.error)
                    }
                }
            }

            FTECH_EKYC_RESULT_TYPE.CANCEL -> {
                if (isActive) {
                    callback?.onCancel()
                } else {
                    pendingCallback = {
                        callback?.onCancel()
                    }
                }
            }
        }
    }

    private fun checkCoolDownAction(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (coolDownTime == -1L || (currentTime - coolDownTime > AppConfig.ACTION_DELAY)) {
            coolDownTime = currentTime
            action.invoke()
        }
    }

    private fun <I : BaseAction.RequestValue, O> runActionInCoroutine(
        action: BaseAction<I, O>,
        request: I,
        callback: IFTechEkycCallback<O>?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            action.invoke(request)
                .onException {
                    CoroutineScope(Dispatchers.Main).launch {
                        invokeCallback(callback, FTechEkycResult<O>().apply {
                            this.type = FTECH_EKYC_RESULT_TYPE.ERROR
                            this.error = if (it is APIException) it else APIException(
                                APIException.UNKNOWN_ERROR,
                                it.message
                            )
                        })
                    }
                }.collect {
                    CoroutineScope(Dispatchers.Main).launch {
                        invokeCallback(callback, FTechEkycResult<O>().apply {
                            this.type = FTECH_EKYC_RESULT_TYPE.SUCCESS
                            this.data = it
                        })
                    }
                }
        }
    }

    // start ekyc
    @JvmStatic
    fun registerEkyc(appId: String, licenseKey: String, callback: IFTechEkycCallback<Boolean>) {
        if (appId.isEmpty()){
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_app_id)
                )
            )
            return
        }
        if (licenseKey.isEmpty()){
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.empty_license_key)
                )
            )
            return
        }
        runActionInCoroutine(
            RegisterEkycAction(),
            RegisterEkycAction.RegisterEkycRV(appId, licenseKey),
            callback = object: IFTechEkycCallback<RegisterEkycData>{
                override fun onSuccess(info: RegisterEkycData) {
                    AppPreferences.token = info.token
                    callback.onSuccess(true)
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    super.onFail(error)
                }
            }
        )
    }


    @JvmStatic
    fun createTransaction(callback: IFTechEkycCallback<TransactionData>) {
        if (!hasTokenRegister()){
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_token_register)
                )
            )
            return
        }
        clearData()
        runActionInCoroutine(
            TransactionAction(),
            BaseAction.VoidRequest(),
            object : IFTechEkycCallback<TransactionData> {
                override fun onSuccess(info: TransactionData) {
                    if (info.transactionId.isNullOrEmpty()) {
                        callback.onFail(
                            APIException(
                                code = APIException.UNKNOWN_ERROR,
                                message = getAppString(R.string.null_or_empty_transaction_id)
                            )
                        )
                    } else {
                        transactionId = info.transactionId.toString()
                        callback.onSuccess(info)
                    }
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    callback.onFail(error)
                }
            }
        )
    }

    @JvmStatic
    fun getProcessTransaction(callback: IFTechEkycCallback<TransactionProcessData>) {
        if (!hasTransactionId()) {
            callback.onFail(
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_transaction_id)
                )
            )
            return
        }
        runActionInCoroutine(
            action = ProcessTransactionAction(),
            request = ProcessTransactionAction.ProcessTransactionRV(transactionId = transactionId),
            callback = object : IFTechEkycCallback<TransactionProcessData> {
                override fun onSuccess(info: TransactionProcessData) {
                    handleProcessTransaction(info)
                    callback.onSuccess(info)
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    callback.onFail(error)
                }
            })
    }

    private fun handleProcessTransaction(info: TransactionProcessData) {
        if (info.processId.isNullOrEmpty()) {
            sessionIdFront = info.sessionIdFront.orEmpty()
            sessionIdBack = info.sessionIdBack.orEmpty()
            sessionIdFace = info.sessionIdFace.orEmpty()
        }
    }

    @JvmStatic
    fun submitInfo(submitInfoRequest: NewSubmitInfoRequest, callback: IFTechEkycCallback<Boolean>) {
        runActionInCoroutine(
            action = NewSubmitInfoAction(),
            request = NewSubmitInfoAction.SubmitRV(request = submitInfoRequest),
            callback = object : IFTechEkycCallback<Boolean> {
                override fun onSuccess(info: Boolean?) {
                    clearData()
                    callback.onSuccess(info)
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    callback.onFail(error)
                }
            }
        )
    }

    @JvmStatic
    fun uploadPhoto(
        pathImage: String,
        orientation: CAPTURE_TYPE,
        callback: IFTechEkycCallback<CaptureData>
    ) {
        if (!hasTransactionId()) {
            callback.onFail(
                APIException(
                    APIException.UNKNOWN_ERROR,
                    getAppString(R.string.null_or_empty_transaction_id)
                )
            )
            return
        }
        runActionInCoroutine(
            action = NewUploadPhotoAction(),
            request = NewUploadPhotoAction.UploadRV(
                absolutePath = pathImage,
                orientation = orientation,
                transactionId = transactionId
            ),
            callback = object : IFTechEkycCallback<CaptureData> {
                override fun onSuccess(info: CaptureData) {
                    if (info.data?.sessionId.isNullOrEmpty()) {
                        callback.onFail(getErrorSessionId(orientation))
                    } else {
                        handleSuccessUploadPhoto(orientation, info)
                        callback.onSuccess(info)
                    }
                }

                override fun onCancel() {
                    callback.onCancel()
                }

                override fun onFail(error: APIException?) {
                    callback.onFail(error)
                }
            }
        )
    }

    private fun handleSuccessUploadPhoto(orientation: CAPTURE_TYPE?, info: CaptureData) {
        when (orientation) {
            CAPTURE_TYPE.BACK -> {
                sessionIdBack = info.data?.sessionId.toString()
            }
            CAPTURE_TYPE.FRONT -> {
                sessionIdFront = info.data?.sessionId.toString()
            }
            CAPTURE_TYPE.FACE -> {
                sessionIdFace = info.data?.sessionId.toString()
            }
            else-> {}
        }
    }

    private fun getErrorSessionId(orientation: CAPTURE_TYPE?): APIException {
        return when (orientation) {
            CAPTURE_TYPE.BACK -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_back)
                )

            }

            CAPTURE_TYPE.FRONT -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_front)
                )
            }

            CAPTURE_TYPE.FACE -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_face)
                )
            }

            else -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_unknown)
                )
            }
        }
    }

    @JvmStatic
    fun faceMatching(
        callback: IFTechEkycCallback<FaceMatchingData>
    ) {
        if (!hasTransactionAndSessionCaptureId()) {
            callback.onFail(
                APIException(
                    APIException.UNKNOWN_ERROR,
                    getAppString(R.string.empty_transaction_id_and_session_capture)
                )
            )
            return
        }
        runActionInCoroutine(
            action = FaceMatchingAction(), request = FaceMatchingAction.FaceMatchingRV(
                transactionId, sessionIdFront, sessionIdBack, sessionIdFace
            ), callback = callback
        )
    }

    private fun hasTransactionAndSessionCaptureId(): Boolean {
        return transactionId.isNotEmpty() && sessionIdFront.isNotEmpty() &&
                sessionIdBack.isNotEmpty() && sessionIdFace.isNotEmpty()
    }

    private fun hasTransactionId(): Boolean = transactionId.isNotEmpty()

    private fun clearData() {
        transactionId = ""
        sessionIdFront = ""
        sessionIdBack = ""
        sessionIdFace = ""
    }

    private fun hasTokenRegister() = !AppPreferences.token.isNullOrEmpty()

}
