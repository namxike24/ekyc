package ai.ftech.fekyc.common

import ai.ftech.fekyc.common.message.HandleApiException
import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.domain.event.ExpireEvent
import ai.ftech.fekyc.publish.FTechEkycManager
import ai.ftech.fekyc.utils.ShareFlowEventBus
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.onException(onCatch: suspend (Throwable) -> Unit): Flow<T> {
    return catch { e ->
        if (e is APIException) {
            val msg = HandleApiException.getAPIMessage(FTechEkycManager.getApplicationContext(), e)
            val apiException = APIException(e.code, msg)
            onCatch(apiException)

//            if (e.code == APIException.EXPIRE_SESSION_ERROR) {
//                ShareFlowEventBus.emitEvent(ExpireEvent())
//            }

        } else {
            onCatch(APIException(APIException.UNKNOWN_ERROR ,e.message))
        }
    }
}

fun Drawable.drawAt(rect: RectF, canvas: Canvas) {
    setBounds(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
    draw(canvas)
}

fun getAppString(
    @StringRes stringId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): String {
    return context?.getString(stringId) ?: ""
}

fun getAppString(
    @StringRes stringId: Int,
    vararg params: Any,
    context: Context? = FTechEkycManager.getApplicationContext()
): String {
    return context?.getString(stringId, *params) ?: ""
}

fun getAppSpannableString(
    @StringRes stringId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): SpannableString {
    return SpannableString(context?.getString(stringId))
}

fun getAppFont(
    @FontRes fontId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): Typeface? {
    return context?.let {
        ResourcesCompat.getFont(it, fontId)
    }
}

fun getAppDrawable(
    @DrawableRes drawableId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): Drawable? {
    return context?.let {
        ContextCompat.getDrawable(it, drawableId)
    }
}

fun getAppDimensionPixel(
    @DimenRes dimenId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): Int {
    return context?.resources?.getDimensionPixelSize(dimenId) ?: -1
}

fun getAppDimension(
    @DimenRes dimenId: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): Float {
    return context?.resources?.getDimension(dimenId) ?: -1f
}

fun getAppColor(
    @ColorRes colorRes: Int,
    context: Context? = FTechEkycManager.getApplicationContext()
): Int {
    return context?.let {
        ContextCompat.getColor(it, colorRes)
    } ?: Color.TRANSPARENT
}
