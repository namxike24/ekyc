package ai.ftech.ekyc.common

import ai.ftech.base.common.BaseActivity
import ai.ftech.base.common.StatusBar
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.activityresultlancher.OpenAppSettingResult
import ai.ftech.ekyc.domain.event.ExpireEvent
import ai.ftech.ekyc.domain.event.FinishActEvent
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import ai.ftech.ekyc.presentation.dialog.LoadingDialog
import ai.ftech.ekyc.presentation.dialog.NotiNetworkDialog
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.home.HomeActivity
import ai.ftech.ekyc.utils.KeyboardUtility
import ai.ftech.ekyc.utils.ShareFlowEventBus
import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

abstract class FEkycActivity(layoutId: Int) : BaseActivity(layoutId), IFEkycContext {

    var warningDialog: WarningCaptureDialog? = null
    var notiNetworkDialog: NotiNetworkDialog? = null
    var loadingDialog: LoadingDialog? = null
    protected val openAppSettingResult by lazy {
        OpenAppSettingResult()
    }

    override fun onDestroy() {
        loadingDialog = null
        super.onDestroy()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
//        setFullScreen()
        loadingDialog = LoadingDialog()
    }

    override fun onInitView() {
        super.onInitView()
        //hide keyboard edittext when touch outside
        KeyboardUtility.hideSoftKeyboard(this, window.decorView.rootView)
        lifecycleScope.launchWhenStarted {
            val flow = ShareFlowEventBus.events.filter {
                it is ExpireEvent
            }

            flow.collectLatest {
                when (it) {
                    is ExpireEvent -> {
                        showExpireDialog {
                            lifecycleScope.launch {
                                ShareFlowEventBus.emitEvent(FinishActEvent())
                            }
                            val intent = Intent()
                            intent.putExtra(HomeActivity.SEND_RESULT_FTECH_EKYC_INFO, "")
                            setResult(RESULT_CANCELED, intent)
                            finish()
                        }
                    }
                }
            }
        }

    }


    override fun setupStatusBar(): StatusBar {
        return StatusBar(color = R.color.fekyc_color_dark_blue, isDarkText = false)
    }


    override fun getActivityContext(): Context {
        return this
    }

    override fun showLoading(message: String) {
        if (loadingDialog != null) {
            loadingDialog?.message = message
            loadingDialog?.showDialog(
                supportFragmentManager,
                loadingDialog!!::class.java.simpleName
            )
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismissDialog()
    }

    override fun showError(msg: String?) {
        showHeaderAlert(HEADER_ALERT_TYPE.ERROR, msg)
    }

    override fun showSuccess(msg: String?) {
    }

    override fun showWarning(msg: String?) {
    }

    override fun onBackPressed() {
        showConfirmDialog()
    }

    fun showKeyboard() {
        KeyboardUtility.showKeyBoard(this)
    }

    fun hideKeyboard() {
        KeyboardUtility.hideSoftKeyboard(this)
    }

    fun showConfirmDialog(exitConsumer: (() -> Unit)? = null) {
        val dialog = ConfirmDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_notification))
            .setContent(getAppString(R.string.fekyc_confirm_notification_content))
            .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
            .setRightTitle(getAppString(R.string.fekyc_confirm_stay))
            .build()
        dialog.listener = object : ConfirmDialog.IListener {
            override fun onLeftClick() {
                exitConsumer?.invoke()
                finish()
            }

            override fun onRightClick() {
                dialog.dismissDialog()
            }
        }
        dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
    }

    fun showPermissionDialog() {
        val dialog = ConfirmDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_confirm_permission_title))
            .setContent(getAppString(R.string.fekyc_confirm_permission_content))
            .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
            .setRightTitle(getAppString(R.string.fekyc_setting))
            .build()
        dialog.listener = object : ConfirmDialog.IListener {
            override fun onLeftClick() {
                dialog.dismissDialog()
            }

            override fun onRightClick() {
                openAppSettingResult.launch(this@FEkycActivity)
            }
        }
        dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
    }

    fun showNotiNetworkDialog() {
        val dialog = NotiNetworkDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_notification))
            .setContent(getAppString(R.string.fekyc_ekyc_noti_network))
            .build()
        dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
    }

    fun showExpireDialog(exitConsumer: (() -> Unit)? = null) {
        val dialog = ConfirmDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_notification))
            .setContent(getAppString(R.string.fekyc_session_expire))
            .setRightTitle(getAppString(R.string.fekyc_confirm_exit))
            .build()
        dialog.listener = object : ConfirmDialog.IListener {
            override fun onRightClick() {
                exitConsumer?.invoke()
            }
        }
        dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
    }

    private fun showHeaderAlert(type: HEADER_ALERT_TYPE, content: String?) {
        HeaderAlertDefault(this) {

        }.show(content, type)
    }

}
