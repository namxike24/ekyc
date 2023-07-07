package ai.ftech.fekyc.publish

import ai.ftech.fekyc.AppConfig
import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.base.extension.setApplication
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.common.onException
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKData
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.domain.action.FaceMatchingAction
import ai.ftech.fekyc.domain.action.InitSDKAction
import ai.ftech.fekyc.domain.action.NewSubmitInfoAction
import ai.ftech.fekyc.domain.action.NewUploadPhotoAction
import ai.ftech.fekyc.domain.action.TransactionAction
import ai.ftech.fekyc.domain.model.capture.CaptureData
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData
import ai.ftech.fekyc.domain.model.submit.SubmitInfo
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
                    callback?.onFail()
                } else {
                    pendingCallback = {
                        callback?.onFail()
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

    fun initSDK(callback: IFTechEkycCallback<InitSDKData>) {
        val applicationInfo = applicationContext?.let {
            getApplicationContext().packageManager.getApplicationInfo(
                it.packageName,
                PackageManager.GET_META_DATA
            )
        }
        val bundle = applicationInfo?.metaData
        val appId = bundle?.getString("sdkId")
        val licenseKey = bundle?.getString("licenseKey")
        runActionInCoroutine(
            InitSDKAction(),
            InitSDKAction.InitSDKRV(appId.toString(), licenseKey.toString()),
            object : IFTechEkycCallback<InitSDKData> {
                override fun onSuccess(info: InitSDKData?) {
                    AppPreferences.token = info?.token
                    super.onSuccess(info)
                }
            }
        )
    }

    fun createTransaction(callback: IFTechEkycCallback<TransactionData>){
        runActionInCoroutine(
            TransactionAction(),
            BaseAction.VoidRequest(),
            callback
        )
    }

    @JvmStatic
    fun submitInfo(info: NewSubmitInfoRequest, callback: IFTechEkycCallback<SubmitInfo>) {
        runActionInCoroutine(
            action = NewSubmitInfoAction(),
            request = NewSubmitInfoAction.SubmitRV(request = info),
            callback = callback
        )
    }

    @JvmStatic
    fun uploadPhoto(
        pathImage: String,
        orientation: String?,
        transactionId: String,
        callback: IFTechEkycCallback<CaptureData>
    ) {
        runActionInCoroutine(
            action = NewUploadPhotoAction(),
            request = NewUploadPhotoAction.UploadRV(
                absolutePath = pathImage,
                orientation = orientation,
                transactionId = transactionId
            ),
            callback = callback
        )
    }

    @JvmStatic
    fun faceMatching(
        idTransaction: String,
        idSessionFront: String,
        idSessionBack: String,
        idSessionFace: String,
        callback: IFTechEkycCallback<FaceMatchingData>
    ) {
        runActionInCoroutine(
            action = FaceMatchingAction(), request = FaceMatchingAction.FaceMatchingRV(
                idTransaction, idSessionFront, idSessionBack, idSessionFace
            ), callback = callback
        )
    }


}
