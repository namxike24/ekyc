package ai.ftech.fekyc.presentation.picture.confirm

import ai.ftech.fekyc.base.adapter.BaseVH
import ai.ftech.fekyc.base.adapter.group.GroupData
import ai.ftech.fekyc.base.adapter.group.GroupVH
import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.R
import ai.ftech.fekyc.common.getAppDrawable
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.fekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.fekyc.domain.model.ekyc.PhotoInfo
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class ConfirmPictureGroup(data: PhotoConfirmDetailInfo) : GroupData<List<PhotoInfo>>(data.photoList) {
    companion object {
        private const val TITLE_VIEW_TYPE = 0
        private const val PHOTO_INFO_VIEW_TYPE = 1
    }

    private val imageLoader = ImageLoaderFactory.glide()
    private var groupTitle: String
    var listener: IListener? = null

    init {
        groupTitle = getGroupTitle(data.photoType)
    }

    fun resetData(info: PhotoConfirmDetailInfo) {
        groupTitle = getGroupTitle(info.photoType)
        data = info.photoList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TITLE_VIEW_TYPE
        else PHOTO_INFO_VIEW_TYPE
    }

    override fun getDataInGroup(positionInGroup: Int): Any {
        val indexOffsetOfTitle = 1
        return if (positionInGroup == 0) groupTitle
        else data[positionInGroup - indexOffsetOfTitle]
    }

    override fun getLayoutResource(viewType: Int): Int {
        return when (viewType) {
            TITLE_VIEW_TYPE -> R.layout.fekyc_title_single_item
            PHOTO_INFO_VIEW_TYPE -> R.layout.fekyc_confirm_picture_item
            else -> INVALID_RESOURCE
        }
    }

    override fun getCount(): Int = 1 + data.size

    override fun onCreateVH(itemView: View, viewType: Int): BaseVH<*>? {
        return when (viewType) {
            TITLE_VIEW_TYPE -> TitleVH(itemView)
            PHOTO_INFO_VIEW_TYPE -> PhotoInfoVH(itemView)
            else -> null
        }
    }

    private fun getGroupTitle(photoType: PHOTO_TYPE?): String {
        return when (photoType) {
            PHOTO_TYPE.SSN,
            PHOTO_TYPE.DRIVER_LICENSE,
            PHOTO_TYPE.PASSPORT -> getAppString(R.string.fekyc_confirm_picture_capture_two_face_papers)
            else -> getAppString(R.string.fekyc_confirm_picture_portrait_myself)
        }
    }

    class TitleVH(view: View) : GroupVH<String, ConfirmPictureGroup>(view) {
        private var tvTitle: TextView

        init {
            tvTitle = view.findViewById(R.id.tvTitleSingleItmContent)
        }

        override fun onBind(data: String) {
            tvTitle.text = data
        }
    }

    inner class PhotoInfoVH(view: View) : GroupVH<PhotoInfo, ConfirmPictureGroup>(view) {
        private var ivPhoto: ImageView
        private var ivIcon: ImageView
        private var tvMessage: TextView

        init {
            ivPhoto = view.findViewById(R.id.ivConfirmPictureItmPhoto)
            ivIcon = view.findViewById(R.id.ivConfirmPictureItmIcon)
            tvMessage = view.findViewById(R.id.tvConfirmPictureItmMessage)

            itemView.setOnSafeClick {
                val item = groupManager?.getItemDataAtAdapterPosition(adapterPosition) as PhotoInfo
                listener?.onClickItem(item)
            }
        }

        override fun onBind(data: PhotoInfo) {
            imageLoader.loadImage(itemView, data.url, ivPhoto, null, true)
            ivIcon.setImageDrawable(getIcon())
            tvMessage.text = getMessage()
        }

        private fun getIcon() = getAppDrawable(R.drawable.fekyc_ic_success_green)

        private fun getMessage() = getAppString(R.string.fekyc_confirm_picture_valid_photo)
    }

    interface IListener {
        fun onClickItem(item: PhotoInfo)
    }
}
