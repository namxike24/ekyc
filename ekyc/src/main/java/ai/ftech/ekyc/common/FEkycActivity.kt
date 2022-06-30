package ai.ftech.ekyc.common

import ai.ftech.base.common.BaseActivity
import ai.ftech.base.common.StatusBar
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.activityresultlancher.OpenAppSettingResult
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import ai.ftech.ekyc.presentation.dialog.LoadingDialog
import ai.ftech.ekyc.presentation.dialog.NotiNetworkDialog
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.utils.KeyboardUtility
import android.content.Context

abstract class FEkycActivity(layoutId: Int) : BaseActivity(layoutId), IFEkycContext {

    var warningDialog: WarningCaptureDialog? = null
    var notiNetworkDialog: NotiNetworkDialog? = null
    var loadingDialog: LoadingDialog? = null
    protected val openAppSettingResult by lazy {
        OpenAppSettingResult()
    }

    override fun onResume() {
        super.onResume()
        loadingDialog = LoadingDialog()
    }

    override fun onPause() {
        super.onPause()
        loadingDialog = null
    }

    override fun onInitView() {
        super.onInitView()
        //hide keyboard edittext when touch outside
        KeyboardUtility.hideSoftKeyboard(this, window.decorView.rootView)
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        setFullScreen()
    }

    override fun setupStatusBar(): StatusBar {
        return StatusBar(isDarkText = false)
    }


    override fun getActivityContext(): Context {
        return this
    }

    override fun showLoading(message: String) {
        if (loadingDialog != null) {
            loadingDialog?.message = message
            loadingDialog?.showDialog(supportFragmentManager, loadingDialog!!::class.java.simpleName)
        }
    }

    override fun hideLoading() {
        loadingDialog?.dismissDialog()
    }

    override fun showError(msg: String?) {
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

    fun showConfirmDialog() {
        val dialog = ConfirmDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_notification))
            .setContent(getAppString(R.string.fekyc_confirm_notification_content))
            .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
            .setRightTitle(getAppString(R.string.fekyc_confirm_stay))
            .build()
        dialog.listener = object : ConfirmDialog.IListener {
            override fun onLeftClick() {
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
}
