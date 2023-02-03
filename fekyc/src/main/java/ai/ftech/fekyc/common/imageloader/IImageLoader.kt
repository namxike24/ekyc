package ai.ftech.fekyc.common.imageloader

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.target.CustomTarget
import java.io.File

interface IImageLoader {
    enum class CornerType {
        ALL,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        OTHER_TOP_LEFT,
        OTHER_TOP_RIGHT,
        OTHER_BOTTOM_LEFT,
        OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT,
        DIAGONAL_FROM_TOP_RIGHT
    }

    fun loadImage(activity: Activity?, url: String?, view: ImageView?, ignoreCache: Boolean = false)
    fun loadImage(fragment: Fragment?, url: String?, view: ImageView?, ignoreCache: Boolean = false)
    fun loadImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImage(
        withView: View?,
        drawable: Drawable?,
        view: ImageView?,
        errorDrawable: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable? = null,
        ignoreCache: Boolean = false,
        onError: () -> Unit,
        onComplete: () -> Unit
    )

    fun loadImageBase64(
        fragment: Fragment?,
        base64: String?,
        view: ImageView?,
        ignoreCache: Boolean = false
    )

    fun loadRoundCornerImage(withView: View?, url: String?, view: ImageView?, corner: Int)
    fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: CornerType?
    )

    fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: CornerType?,
        errorDrawable: Drawable? = null
    )

    fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: CornerType?,
        errorDrawable: Drawable? = null,
        onError: () -> Unit,
        onComplete: () -> Unit
    )

    fun loadCircleImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        ignoreCache: Boolean = false
    )

    fun loadCircleImage(withView: View?, file: File?, view: ImageView?)
    fun loadCircleImage(withView: View?, bitmap: Bitmap?, view: ImageView?)
    fun loadCircleImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadCircleImage(withView: View?, resourceDrawable: Drawable?, view: ImageView?)
    fun loadImageNoCache(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable? = null
    )

    fun loadImageToCusTomTarget(
        withView: View?,
        url: String?, target: CustomTarget<Drawable>
    )
}
