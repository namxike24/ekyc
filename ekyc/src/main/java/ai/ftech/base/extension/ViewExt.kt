package ai.ftech.base.extension

import ai.ftech.base.common.BaseActivity
import ai.ftech.base.common.BaseFragment
import ai.ftech.base.common.DebouncedOnClickListener
import ai.ftech.base.common.GlobalDebouncedOnClickListener
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.*

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.shareParentFragmentViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) {
    createViewModelLazy(VM::class, {
        requireParentFragment().viewModelStore
    }, factoryProducer ?: {
        requireParentFragment().defaultViewModelProviderFactory
    })
}

fun <T> BaseActivity.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(this, Observer(onChange))
}

fun <T> BaseFragment.observer(liveData: LiveData<T>, onChange: (T?) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(onChange))
}

fun BaseActivity.runIfNotDestroyed(task: () -> Any?) {
    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            !isDestroyed
        } else {
            true
        }
    ) {
        task()
    }
}

fun BaseFragment.runIfNotDestroyed(task: () -> Any?) {
    if (this.lifecycle.currentState != Lifecycle.State.DESTROYED) {
        task()
    }
}

fun Any.runOnMainThread(task: () -> Any?, delayMs: Long = 0L) {
    Handler(Looper.getMainLooper()).postDelayed({
        when (this) {
            is BaseActivity -> {
                runIfNotDestroyed { task() }
            }
            is BaseFragment -> {
                runIfNotDestroyed { task() }
            }
            else -> {
                task()
            }
        }
    }, delayMs)
}

fun View.setOnSafeClick(
    delayBetweenClicks: Long = ai.ftech.base.common.DEFAULT_DEBOUNCE_INTERVAL,
    click: (view: View) -> Unit
) {
    setOnClickListener(object : DebouncedOnClickListener(delayBetweenClicks) {
        override fun onDebouncedClick(v: View) = click(v)
    })
}

fun View.setOnSafeGlobalClick(
    delayBetweenClicks: Long = ai.ftech.base.common.DEFAULT_DEBOUNCE_INTERVAL,
    click: (view: View) -> Unit
) {
    setOnClickListener(object : GlobalDebouncedOnClickListener(delayBetweenClicks) {
        override fun onDebouncedClick(v: View) = click(v)
    })
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun getAppString(
    @StringRes stringId: Int,
    context: Context? = getApplication()
): String {
    return context?.getString(stringId) ?: ""
}

fun getAppString(
    @StringRes stringId: Int,
    vararg params: Any,
    context: Context? = getApplication()
): String {
    return context?.getString(stringId, *params) ?: ""
}

fun getAppSpannableString(
    @StringRes stringId: Int,
    context: Context? = getApplication()
): SpannableString {
    return SpannableString(context?.getString(stringId))
}

fun getAppFont(
    @FontRes fontId: Int,
    context: Context? = getApplication()
): Typeface? {
    return context?.let {
        ResourcesCompat.getFont(it, fontId)
    }
}

fun getAppDrawable(
    @DrawableRes drawableId: Int,
    context: Context? = getApplication()
): Drawable? {
    return context?.let {
        ContextCompat.getDrawable(it, drawableId)
    }
}

fun getAppDimensionPixel(
    @DimenRes dimenId: Int,
    context: Context? = getApplication()
): Int {
    return context?.resources?.getDimensionPixelSize(dimenId) ?: -1
}

fun getAppDimension(
    @DimenRes dimenId: Int,
    context: Context? = getApplication()
): Float {
    return context?.resources?.getDimension(dimenId) ?: -1f
}

fun getAppColor(
    @ColorRes colorRes: Int,
    context: Context? = getApplication()
): Int {
    return context?.let {
        ContextCompat.getColor(it, colorRes)
    } ?: Color.TRANSPARENT
}

fun Activity.getScreenHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

//fun getAppAnim(
//    @AnimRes animRes: Int,
//    context: Context? = getApplication()
//): Int {
//    return context?.resources.getAnimation(animRes) ?: -1
//}
