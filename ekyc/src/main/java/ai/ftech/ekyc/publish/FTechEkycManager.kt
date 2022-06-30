package ai.ftech.ekyc.publish

import ai.ftech.base.extension.setApplication
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.presentation.home.HomeActivity
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.fragment.app.FragmentActivity
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
                context.registerForActivityResult(object : ActivityResultContract<Unit, FTechEkycResult<FTechEkycInfo>>() {
                    override fun createIntent(context: Context, input: Unit?): Intent {
                        return Intent(context, HomeActivity::class.java)
                    }

                    override fun parseResult(resultCode: Int, intent: Intent?): FTechEkycResult<FTechEkycInfo> {
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
    fun startEkyc(ftechKey: String, appId: String, transactionId: String, callBack: IFTechEkycCallback<FTechEkycInfo>) {
        this.ftechKey = ftechKey
        this.appId = appId
        this.transactionId = transactionId
        checkCoolDownAction {
            callback = callBack
            resultLauncher?.launch()
        }
    }

    private fun parseDataFormActivityForResult(resultCode: Int, intent: Intent?): FTechEkycResult<FTechEkycInfo> {
        val info = intent?.getSerializableExtra(HomeActivity.SEND_RESULT_FTECH_EKYC_INFO) as? FTechEkycInfo

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
}
