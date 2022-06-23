package ai.ftech.ekyc.common

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.common.StatusBar
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import ai.ftech.ekyc.presentation.dialog.NotiNetworkDialog
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.utils.KeyboardUtility
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

abstract class FEkycActivity(layoutId: Int) : BaseActivity(layoutId) {

    var viewTouchOutside: View? = null
    var listener: ITouchOutsideViewListener? = null
        private set
    var warningDialog: WarningCaptureDialog? = null
    var notiNetworkDialog: NotiNetworkDialog? = null

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        setFullScreen()
    }

    override fun setupStatusBar(): StatusBar {
        return StatusBar(isDarkText = false)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            if (listener != null && viewTouchOutside != null && viewTouchOutside?.visibility == View.VISIBLE) {
                val rect = Rect()
                viewTouchOutside?.getGlobalVisibleRect(rect)
                if (rect.contains((ev.rawX).toInt(), (ev.rawY).toInt())) {
                    listener?.onTouchOutside(viewTouchOutside!!, ev)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun showKeyboard() {
        KeyboardUtility.showKeyBoard(this)
    }

    fun hideKeyboard() {
        KeyboardUtility.hideSoftKeyboard(this)
    }

    fun addTouchRootListener(view: View, listener: ITouchOutsideViewListener) {
        setOnTouchOutsideViewListener(view, listener)
    }

    fun removeTouchRootListener() {
        setOnTouchOutsideViewListener(null, null)
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

    fun showNotiNetworkDialog() {
        val dialog = NotiNetworkDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_notification))
            .setContent(getAppString(R.string.fekyc_ekyc_noti_network))
            .build()
        dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
    }

    private fun setOnTouchOutsideViewListener(view: View?, listener: ITouchOutsideViewListener?) {
        this.viewTouchOutside = view
        this.listener = listener
    }

    interface ITouchOutsideViewListener {
        fun onTouchOutside(view: View, event: MotionEvent)
    }
}
