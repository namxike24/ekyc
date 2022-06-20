package ai.ftech.ekyc.common

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.ekyc.utils.KeyboardUtility
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

abstract class FEkycActivity(layoutId: Int) : BaseActivity(layoutId) {

    var viewTouchOutside: View? = null
    var listener: ITouchOutsideViewListener? = null
        private set

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        setFullScreen()
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

    private fun setOnTouchOutsideViewListener(view: View?, listener: ITouchOutsideViewListener?) {
        this.viewTouchOutside = view
        this.listener = listener
    }

    interface ITouchOutsideViewListener {
        fun onTouchOutside(view: View, event: MotionEvent)
    }
}
