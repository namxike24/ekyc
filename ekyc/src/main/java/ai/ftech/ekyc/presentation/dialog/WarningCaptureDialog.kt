package ai.ftech.ekyc.presentation.dialog

import ai.ftech.dev.base.common.BaseDialog
import ai.ftech.dev.base.common.DialogScreen
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.getAppDrawable
import ai.ftech.ekyc.common.getAppString
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

typealias WARNING_TYPE = WarningCaptureDialog.WarningInfo.WARNING_TYPE

class WarningCaptureDialog(private var type: WARNING_TYPE) : BaseDialog(R.layout.fekyc_warning_capture_dialog) {

    private lateinit var tvTitle: TextView
    private lateinit var rvWarningList: RecyclerView
    private lateinit var btnConfirmOK: Button
    private val adapter by lazy {
        WarningAdapter(type)
    }

    override fun onInitView() {
        tvTitle = viewRoot.findViewById(R.id.tvWarningCaptureDlgTitle)
        rvWarningList = viewRoot.findViewById(R.id.rvWarningCaptureDlgWarningList)
        btnConfirmOK = viewRoot.findViewById(R.id.btnWarningCaptureDlgConfirm)

        tvTitle.text = getTitle()

        rvWarningList.layoutManager = getLayoutManager()
        rvWarningList.adapter = adapter

        btnConfirmOK.setOnSafeClick {
            dismissDialog()
        }
    }

    private fun getTitle(): String {
        return when (type) {
            WARNING_TYPE.PAPERS -> getAppString(R.string.fekyc_warning_papers_title)
            WARNING_TYPE.PORTRAIT -> getAppString(R.string.fekyc_warning_portrait_title)
        }
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager? {
        if (activity != null) {
            return when (type) {
                WARNING_TYPE.PAPERS -> GridLayoutManager(activity, adapter.itemCount, GridLayoutManager.HORIZONTAL, false)
                WARNING_TYPE.PORTRAIT -> GridLayoutManager(activity, adapter.itemCount, GridLayoutManager.VERTICAL, false)
            }
        }
        return null
    }

    override fun getBackgroundId() = R.id.frameWarningCaptureDlgRoot

    override fun screen(): DialogScreen {
        return DialogScreen().apply {
            mode = DialogScreen.DIALOG_MODE.BOTTOM
            isFullWidth = true
        }
    }

    private class WarningAdapter(private var type: WarningInfo.WARNING_TYPE) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        companion object {
            const val INVALID_RESOURCE = -1
            const val PAPERS_VIEW_TYPE = 0
            const val PORTRAIT_VIEW_TYPE = 1
        }

        private var dataList: List<WarningDisplay>? = null


        init {
            dataList = initData()
        }

        override fun getItemViewType(position: Int): Int {
            return when (type) {
                WARNING_TYPE.PAPERS -> PAPERS_VIEW_TYPE
                WARNING_TYPE.PORTRAIT -> PORTRAIT_VIEW_TYPE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layout = when (viewType) {
                PAPERS_VIEW_TYPE -> R.layout.fekyc_warning_papers_item
                PORTRAIT_VIEW_TYPE -> R.layout.fekyc_warning_portrait_item
                else -> INVALID_RESOURCE
            }

            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

            return when (viewType) {
                PAPERS_VIEW_TYPE -> WarningPapersVH(view)
                else -> WarningPortraitVH(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = dataList?.get(position)
            if (item != null) {
                if (holder is WarningPapersVH) holder.onBind(item)
                if (holder is WarningPortraitVH) holder.onBind(item)
            }
        }

        override fun getItemCount() = dataList?.size ?: 0

        private fun initData(): List<WarningDisplay> {
            return getDataList().mapIndexed { index, value ->
                WarningDisplay(value).apply {
                    this.index = index
                }
            }
        }

        private fun getDataList(): List<WarningInfo> {
            return when (type) {
                WARNING_TYPE.PAPERS -> {
                    listOf(
                        WarningInfo(getAppString(R.string.fekyc_warning_papers_0)),
                        WarningInfo(getAppString(R.string.fekyc_warning_papers_1)),
                        WarningInfo(getAppString(R.string.fekyc_warning_papers_2))
                    )
                }
                WARNING_TYPE.PORTRAIT -> {
                    listOf(
                        WarningInfo(getAppString(R.string.fekyc_warning_portrait_0)),
                        WarningInfo(getAppString(R.string.fekyc_warning_portrait_1)),
                        WarningInfo(getAppString(R.string.fekyc_warning_portrait_2))
                    )
                }
            }
        }

        inner class WarningPapersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var ivImage: ImageView
            private var tvContent: TextView

            init {
                ivImage = itemView.findViewById(R.id.ivWarningPapersItmIcon)
                tvContent = itemView.findViewById(R.id.tvWarningPapersItmContent)
            }

            fun onBind(data: WarningDisplay) {
                ivImage.setImageDrawable(data.getIcon())
                tvContent.text = data.data.content
            }
        }

        inner class WarningPortraitVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var ivImage: ImageView
            private var ivIcon: ImageView
            private var tvContent: TextView

            init {
                ivImage = itemView.findViewById(R.id.ivWarningPortraitItmImage)
                ivIcon = itemView.findViewById(R.id.ivWarningPortraitItmIcon)
                tvContent = itemView.findViewById(R.id.ivWarningPortraitItmContent)
            }

            fun onBind(data: WarningDisplay) {
                ivImage.setImageDrawable(data.getImage())
                ivIcon.setImageDrawable(data.getIcon())
                tvContent.text = data.getContent()
            }
        }

        inner class WarningDisplay(val data: WarningInfo) {
            var index: Int? = null

            fun getImage(): Drawable? {
                if (type == WARNING_TYPE.PORTRAIT) {
                    return when (index) {
                        0 -> getAppDrawable(R.drawable.fekyc_ic_sun_light)
                        1 -> getAppDrawable(R.drawable.fekyc_ic_human_glass)
                        2 -> getAppDrawable(R.drawable.fekyc_ic_human_facemask)
                        else -> null
                    }
                }
                return null
            }

            fun getIcon(): Drawable? {
                return when (type) {
                    WARNING_TYPE.PAPERS -> getAppDrawable(R.drawable.fekyc_ic_warning_blue)
                    WARNING_TYPE.PORTRAIT -> {
                        when (index) {
                            0 -> getAppDrawable(R.drawable.fekyc_ic_success_green)
                            1 -> getAppDrawable(R.drawable.fekyc_ic_error_red)
                            2 -> getAppDrawable(R.drawable.fekyc_ic_error_red)
                            else -> null
                        }
                    }
                }
            }

            fun getContent(): String {
                return data.content
            }
        }
    }

    data class WarningInfo(val content: String) {
        enum class WARNING_TYPE {
            PAPERS, PORTRAIT
        }
    }
}
