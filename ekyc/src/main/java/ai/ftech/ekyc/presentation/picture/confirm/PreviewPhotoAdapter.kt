package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.ekyc.common.imageloader.IImageLoader
import ai.ftech.ekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class PreviewPhotoAdapter : PagerAdapter() {

    private val imageLoader: IImageLoader = ImageLoaderFactory.glide()
    var dataList: List<PhotoInfo>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getCount() = 299

    override fun isViewFromObject(view: View, data: Any): Boolean {
        return view === data
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val ivPhoto = ImageView(container.context)

        ivPhoto.post {
            ivPhoto.scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        container.addView(ivPhoto)

        imageLoader.loadImage(
            container,
            dataList?.get(position % 3)?.url!!,
            ivPhoto,
            null,
            true
        )

        return ivPhoto
    }

    override fun destroyItem(container: ViewGroup, position: Int, data: Any) {
        (container as ViewPager).removeView(data as? View)
    }
}
