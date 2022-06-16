package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.ekyc.domain.model.PhotoInfo
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConfirmPictureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val INVALID_RESOURCE = -1
        private const val INVALID_VIEW_TYPE = -2
        private const val TITLE_VIEW_TYPE = 0
        private const val PHOTO_INFO_VIEW_TYPE = 1
    }

    private val imageLoader = ImageLoaderFactory.glide()
    private var dataList: List<Any>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(list: List<Any>?) {
        val tempList = list?.toMutableList()

        tempList?.forEachIndexed { index, value ->
            if (value is PhotoInfo) {
                tempList[index] = PhotoInfoDisplay(value)
            }
        }

        dataList = tempList

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataList?.get(position)) {
            is SingleTitle -> TITLE_VIEW_TYPE
            is PhotoInfoDisplay -> PHOTO_INFO_VIEW_TYPE
            else -> INVALID_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = dataList?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when (viewType) {
            TITLE_VIEW_TYPE -> R.layout.fekyc_title_single_item
            PHOTO_INFO_VIEW_TYPE -> R.layout.fekyc_confirm_picture_item
            else -> INVALID_RESOURCE
        }

        Log.d("anhnd", "onCreateViewHolder: $layout")

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return when (viewType) {
            TITLE_VIEW_TYPE -> TitleVH(view)
            else -> PhotoInfoVH(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList?.get(position)
        if (item != null) {
            if (holder is TitleVH) holder.onBind(item as SingleTitle)
            if (holder is PhotoInfoVH) holder.onBind(item as PhotoInfoDisplay)
        }
    }

    inner class TitleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitleSingleItmContent)
        }

        fun onBind(data: SingleTitle) {
            tvTitle.text = data.title
        }
    }

    inner class PhotoInfoVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivPhoto: ImageView
        private var ivIcon: ImageView
        private var tvMessage: TextView

        init {
            ivPhoto = itemView.findViewById(R.id.ivConfirmPictureItmPhoto)
            ivIcon = itemView.findViewById(R.id.ivConfirmPictureItmIcon)
            tvMessage = itemView.findViewById(R.id.tvConfirmPictureItmMessage)
        }

        fun onBind(data: PhotoInfoDisplay) {
            imageLoader.loadImage(itemView, data.getUrl(), ivPhoto, null, true)
            ivIcon.setImageDrawable(data.getIcon())
            tvMessage.text = data.getMessage()
        }
    }

    class PhotoInfoDisplay(val data: PhotoInfo) {
        fun getUrl(): String {
            return data.url.toString()
        }

        fun getIcon(): Drawable? {
            return getAppDrawable(R.drawable.fekyc_ic_success_green)
        }

        fun getMessage(): String? {
            if (data.isValid) {
                return getAppString(R.string.fekyc_confirm_picture_valid_photo)
            }
            return null
        }
    }

    class SingleTitle {
        var title: String? = null
    }
}
