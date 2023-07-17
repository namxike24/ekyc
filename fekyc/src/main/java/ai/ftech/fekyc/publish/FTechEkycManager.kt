package ai.ftech.fekyc.publish

import ai.ftech.fekyc.AppConfig
import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.base.extension.setApplication
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.common.onException
import ai.ftech.fekyc.data.repo.converter.FaceMatchingDataConvertToSubmitRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.RegisterEkycData
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.domain.action.FaceMatchingAction
import ai.ftech.fekyc.domain.action.NewSubmitInfoAction
import ai.ftech.fekyc.domain.action.NewUploadPhotoAction
import ai.ftech.fekyc.domain.action.RegisterEkycAction
import ai.ftech.fekyc.domain.action.TransactionAction
import ai.ftech.fekyc.domain.model.capture.CaptureData
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData
import ai.ftech.fekyc.infras.EncodeRSA
import ai.ftech.fekyc.presentation.AppPreferences
import ai.ftech.fekyc.presentation.home.HomeActivity
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

    private var submitInfoRequest: NewSubmitInfoRequest? = null

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
        transactionId: String,
        callBack: IFTechEkycCallback<FTechEkycInfo>
    ) {
        this.ftechKey = EncodeRSA.encryptData(licenseKey, applicationContext?.packageName)
        this.appId = appId
        this.transactionId = "${transactionId}_${appId}"
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
    fun registerEkyc(callback: IFTechEkycCallback<RegisterEkycData>) {
        val applicationInfo = applicationContext?.let {
            getApplicationContext().packageManager.getApplicationInfo(
                it.packageName,
                PackageManager.GET_META_DATA
            )
        }
        val bundle = applicationInfo?.metaData
        val appId = bundle?.getString("ekycId")
        val licenseKey = bundle?.getString("licenseKey")
        runActionInCoroutine(
            RegisterEkycAction(),
            RegisterEkycAction.RegisterEkycRV(appId.toString(), licenseKey.toString()),
            callback
        )
    }


    @JvmStatic
    fun createTransaction(callback: IFTechEkycCallback<TransactionData>) {
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
    fun submitInfo(callback: IFTechEkycCallback<Boolean>) {
        if (hasInfoSubmit()) {
            runActionInCoroutine(
                action = NewSubmitInfoAction(),
                request = NewSubmitInfoAction.SubmitRV(request = submitInfoRequest!!),
                callback = object : IFTechEkycCallback<Boolean> {
                    override fun onSuccess(info: Boolean?) {
                        clearDataWhenSubmitSuccess()
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
        } else {
            callback.onFail(
                APIException(
                    APIException.UNKNOWN_ERROR,
                    getAppString(R.string.null_submit_info_request)
                )
            )
        }
    }

    @JvmStatic
    fun uploadPhoto(
        pathImage: String,
        orientation: String?,
        callback: IFTechEkycCallback<CaptureData>
    ) {
        if (hasTransactionId()) {
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
        } else {
            callback.onFail(
                APIException(
                    APIException.UNKNOWN_ERROR,
                    getAppString(R.string.null_or_empty_transaction_id)
                )
            )
        }
    }

    private fun handleSuccessUploadPhoto(orientation: String?, info: CaptureData) {
        when (orientation) {
            "back" -> {
                sessionIdBack = info.data?.sessionId.toString()
            }

            "front" -> {
                sessionIdFront = info.data?.sessionId.toString()
            }
            null -> {
                sessionIdFace = info.data?.sessionId.toString()

            }
        }
    }

    private fun getErrorSessionId(orientation: String?): APIException {
        return when (orientation) {
            "back" -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_back)
                )

            }

            "front" -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_front)
                )
            }

            null -> {
                APIException(
                    code = APIException.UNKNOWN_ERROR,
                    message = getAppString(R.string.null_or_empty_session_id_face)
                )
            }

            else -> {
                APIException(code = APIException.UNKNOWN_ERROR)
            }
        }
    }

    @JvmStatic
    fun faceMatching(
        callback: IFTechEkycCallback<FaceMatchingData>
    ) {
        if (hasTransactionAndSessionCaptureId()) {
            runActionInCoroutine(
                action = FaceMatchingAction(), request = FaceMatchingAction.FaceMatchingRV(
                    transactionId, sessionIdFront, sessionIdBack, sessionIdFace
                ), callback = object : IFTechEkycCallback<FaceMatchingData> {
                    override fun onSuccess(info: FaceMatchingData) {
                        submitInfoRequest = FaceMatchingDataConvertToSubmitRequest().convert(info)
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
        } else {
            callback.onFail(
                APIException(
                    APIException.UNKNOWN_ERROR,
                    getAppString(R.string.empty_transaction_id_and_session_capture)
                )
            )
        }
    }

    private fun hasTransactionAndSessionCaptureId(): Boolean {
        return transactionId.isNotEmpty() && sessionIdFront.isNotEmpty() &&
                sessionIdBack.isNotEmpty() && sessionIdFace.isNotEmpty()
    }

    private fun hasTransactionId(): Boolean = transactionId.isNotEmpty()

    private fun hasInfoSubmit(): Boolean = submitInfoRequest != null

    private fun clearDataWhenSubmitSuccess(){
        submitInfoRequest = null
        transactionId = ""
        sessionIdFront = ""
        sessionIdBack = ""
        sessionIdFace = ""
    }

}
