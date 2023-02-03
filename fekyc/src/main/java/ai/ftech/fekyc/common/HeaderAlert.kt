package ai.ftech.fekyc.common

import ai.ftech.fekyc.R
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.TextView

internal interface IHeaderAlert {
    fun show(msg: String?, type: HEADER_ALERT_TYPE)
    fun dismiss()
    fun destroy()
}

enum class HEADER_ALERT_TYPE {
    ERROR, WARNING, SUCCESS
}

enum class HEADER_ALERT_TIME_SHOWN(val time: Long) {
    DELAY_DEFAULT(2000L),
    DELAY_1_HALF_SECOND(1500L),
    DELAY_2_SECOND(2000L),
    DELAY_2_5_SECOND(2500L),
    DELAY_3_SECOND(3000L)
}

abstract class HeaderAlert(val activity: Activity, val itemConsumer: (() -> Unit)?) : IHeaderAlert {
    var message: String? = null
    var alertView: ViewGroup? = null
    private var isShowing = false

    override fun show(msg: String?, type: HEADER_ALERT_TYPE) {
        if (isShowing) {
            if (msg != message) {
                enqueueMessage(msg, type)
            }
            return
        }

        isShowing = true
        message = msg
        if (alertView == null) {
            alertView = onCreateView(activity, type)
            addView(alertView!!)
        }

        onViewCreated(alertView!!, type)
        showAnim(alertView!!, type)
    }

    override fun dismiss() {
        message = null
        dismissAnim(alertView!!)
        itemConsumer?.invoke()
    }

    abstract fun onCreateView(activity: Activity, type: HEADER_ALERT_TYPE): ViewGroup
    abstract fun onViewCreated(view: ViewGroup, type: HEADER_ALERT_TYPE)

    open fun addView(view: ViewGroup) {
        activity.window.addContentView(
            alertView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    open fun showAnim(view: ViewGroup, type: HEADER_ALERT_TYPE) {
        val animDown = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom)
        val animController = LayoutAnimationController(animDown)

        view.visibility = View.VISIBLE
        view.layoutAnimation = animController
        animDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onShow(type)
                //Log.e("HeaderAlert", "onSHow")
            }

            override fun onAnimationStart(animation: Animation?) {
                //Log.e("HeaderAlert", "onStart")
            }
        })
        view.startAnimation(animDown)

        /* view.visibility = View.VISIBLE
         view.post {
             Log.e("HeaderAlert", "onSHow")
             view.translationY = -view.height.toFloat()
             view.animate().translationYBy(view.height.toFloat())
         }*/
    }

    open fun dismissAnim(view: ViewGroup) {
        val animUp = AnimationUtils.loadAnimation(activity, R.anim.slide_out_top)
        val animController = LayoutAnimationController(animUp)

        view.visibility = View.GONE
        view.layoutAnimation = animController
        animUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                isShowing = false
                onDismiss()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

        view.startAnimation(animUp)
    }

    open fun onShow(type: HEADER_ALERT_TYPE) {}
    open fun onDismiss() {}
    open fun enqueueMessage(msg: String?, type: HEADER_ALERT_TYPE) {}
}

open class HeaderAlertDefault(activity: Activity, itemConsumer: (() -> Unit)?) : HeaderAlert(activity, itemConsumer) {

    var timeShown = HEADER_ALERT_TIME_SHOWN.DELAY_2_5_SECOND
    private val messageEnqueue = arrayListOf<MessageInfo>()
    private val handler = Handler(Looper.getMainLooper())
    private val dismissRunnable = Runnable {
        dismiss()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(activity: Activity, type: HEADER_ALERT_TYPE): ViewGroup {
        return LayoutInflater.from(activity)
            .inflate(R.layout.ekyc_layout_header_alert, null) as ViewGroup
    }

    override fun onViewCreated(view: ViewGroup, type: HEADER_ALERT_TYPE) {
//        if ((activity.window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//            || (activity.window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        ) {
////            view.setPadding(view.paddingLeft, view.resources.getDimensionPixelSize(R.dimen.dimen_20) + activity.getStatusBarHeight(), view.paddingRight, view.paddingBottom)
//        }
//        val ivIcon = view.findViewById<ImageView>(R.id.ivHeaderAlert)
        val tvContent = view.findViewById<TextView>(R.id.tvHeaderAlert)

        if (type == HEADER_ALERT_TYPE.ERROR || type == HEADER_ALERT_TYPE.WARNING) {
            view.setBackgroundResource(R.color.fekyc_color_failure)
            tvContent.setTextColor(getAppColor(R.color.fbase_color_white))
//            ivIcon.setImageResource(R.drawable.fid_ic_alert_error)
        } else {
//            view.setBackgroundResource(R.color.fid_alert_green_background)
//            tvContent.setTextColor(getAppColor(R.color.fid_green))
//            ivIcon.setImageResource(R.drawable.fid_ic_alert_success)
        }
        tvContent.text = message
        handler.postDelayed(dismissRunnable, timeShown.time)
    }

    override fun onDismiss() {
        super.onDismiss()
        if (messageEnqueue.isNotEmpty()) {
            val nextMsg = messageEnqueue.removeAt(0)
            show(nextMsg.msg, nextMsg.type!!)
        }
        handler.removeCallbacks(dismissRunnable)
    }

    /*override fun show(msg: String?, type: HEADER_ALERT_TYPE) {
        handler.removeCallbacks(dismissRunnable)
        super.show(msg, type)
    }*/

    override fun destroy() {
        handler.removeCallbacks(dismissRunnable)
        messageEnqueue.clear()
    }

    override fun enqueueMessage(msg: String?, type: HEADER_ALERT_TYPE) {
        super.enqueueMessage(msg, type)
        var exist = false
        messageEnqueue.forEach {
            if (it.isSame(msg)) {
                exist = true
                return@forEach
            }
        }
        if (!exist) {
            val msgInfo = MessageInfo()
            msgInfo.msg = msg
            msgInfo.type = type
            messageEnqueue.add(msgInfo)
        }
    }


    private class MessageInfo {
        var type: HEADER_ALERT_TYPE? = null
        var msg: String? = null

        fun isSame(msg: String?): Boolean {
            return this.msg == msg
        }
    }
}
