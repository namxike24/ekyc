package ai.ftech.fekyc.common.imageloader

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File

class GlideImageLoaderImpl : IImageLoader {

    override fun loadImage(
        activity: Activity?,
        url: String?,
        view: ImageView?,
        ignoreCache: Boolean
    ) {
        Glide.with(activity!!).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(
        fragment: Fragment?,
        url: String?,
        view: ImageView?,
        ignoreCache: Boolean
    ) {
        Glide.with(fragment!!).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable?,
        ignoreCache: Boolean
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).setDefaultRequestOptions(requestOptions).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(
        withView: View?,
        drawable: Drawable?,
        view: ImageView?,
        errorDrawable: Drawable?,
        ignoreCache: Boolean
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).setDefaultRequestOptions(requestOptions).load(drawable).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.into(view!!)
    }

    override fun loadImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable?,
        ignoreCache: Boolean,
        onError: () -> Unit, onComplete: () -> Unit
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).setDefaultRequestOptions(requestOptions).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                onError.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onComplete.invoke()
                return false
            }

        })
            .into(view!!)
    }

    override fun loadImageBase64(
        fragment: Fragment?,
        base64: String?,
        view: ImageView?,
        ignoreCache: Boolean
    ) {
        val imageByteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        if (view != null) {
            Glide.with(fragment!!)
                .load(imageByteArray)
                .into(view)
        }
    }

    override fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int
    ) {
        Glide.with(withView!!).load(url)
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(corner))).into(view!!)
    }

    override fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: IImageLoader.CornerType?
    ) {
        Glide.with(withView!!).load(url)
            .apply(
                RequestOptions().transforms(
                    CenterCrop(),
                    RoundedCornersTransformation(
                        corner, 0,
                        RoundedCornersTransformation.CornerType.valueOf(cornerType.toString())
                    )
                )
            )
            .into(view!!)
    }

    override fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: IImageLoader.CornerType?,
        errorDrawable: Drawable?
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).load(url)
            .apply(
                requestOptions.transforms(
                    CenterCrop(),
                    RoundedCornersTransformation(
                        corner, 0,
                        RoundedCornersTransformation.CornerType.valueOf(cornerType.toString())
                    )
                )
            )
            .into(view!!)
    }

    override fun loadRoundCornerImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        corner: Int,
        cornerType: IImageLoader.CornerType?,
        errorDrawable: Drawable?,
        onError: () -> Unit,
        onComplete: () -> Unit
    ) {
        val requestOptions = RequestOptions().apply {
            error(errorDrawable)
        }
        Glide.with(withView!!).load(url)
            .apply(
                requestOptions.transforms(
                    CenterCrop(),
                    RoundedCornersTransformation(
                        corner,
                        0,
                        RoundedCornersTransformation.CornerType.valueOf(cornerType.toString())
                    )
                )
            )
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onError.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onComplete.invoke()
                    return false
                }

            })
            .into(view!!)
    }

    override fun loadCircleImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        ignoreCache: Boolean
    ) {
//        Glide.with(withView!!).load(url).apply {
//            if (ignoreCache) {
//                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
//            }
//        }.apply(RequestOptions.circleCropTransform()).into(view!!)
        Glide.with(withView!!).load(url).transform(CircleCrop()).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
            RequestOptions
                .bitmapTransform(RoundedCornersTransformation(withView.width / 2, 0))
                .override(withView.width, withView.height)

        }.into(view!!)
    }

    override fun loadCircleImage(withView: View?, file: File?, view: ImageView?) {
        Glide.with(withView!!).load(file).apply(RequestOptions.circleCropTransform()).into(view!!)
    }

    override fun loadCircleImage(withView: View?, bitmap: Bitmap?, view: ImageView?) {
        Glide.with(withView!!).load(bitmap).apply(RequestOptions.circleCropTransform()).into(view!!)
    }

    override fun loadCircleImage(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable?,
        ignoreCache: Boolean
    ) {
        Glide.with(withView!!).load(url).apply {
            if (ignoreCache) {
                skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            }
        }.apply(RequestOptions.circleCropTransform().error(errorDrawable)).into(view!!)
    }

    override fun loadCircleImage(
        withView: View?,
        resourceDrawable: Drawable?,
        view: ImageView?,
    ) {
        Glide.with(withView!!).load(resourceDrawable).apply(RequestOptions.circleCropTransform())
            .into(view!!)
    }

    override fun loadImageToCusTomTarget(
        withView: View?,
        url: String?, target: CustomTarget<Drawable>
    ) {
        Glide.with(withView!!).load(url)
            .apply(RequestOptions().transforms(CenterCrop())).into(target)
    }

    override fun loadImageNoCache(
        withView: View?,
        url: String?,
        view: ImageView?,
        errorDrawable: Drawable?
    ) {
        Glide.with(withView!!).load(url)
            .error(errorDrawable)
            .fitCenter()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view!!)
    }
}
