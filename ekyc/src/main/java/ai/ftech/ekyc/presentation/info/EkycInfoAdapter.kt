package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.adapter.BaseAdapter
import ai.ftech.dev.base.adapter.BaseVH
import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.EkycInfo
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class EkycInfoAdapter : BaseAdapter() {

    override fun getLayoutResource(viewType: Int) = R.layout.fekyc_ekyc_info_item

    override fun getDataAtPosition(position: Int): UserInfo {
        return (dataList[position] as UserInfoDisplay).data
    }

    override fun onCreateViewHolder(viewType: Int, view: View): BaseVH<*> {
        return UserInfoVH(view)
    }

    inner class UserInfoVH(view: View) : BaseVH<UserInfoDisplay>(view) {
        private var tvTitle: TextView
        private var edtValue: EditText
        private var ivIcon: ImageView

        init {
            tvTitle = view.findViewById(R.id.tvEkycInfoItmTitle)
            edtValue = view.findViewById(R.id.tvEkycInfoItmValue)
            ivIcon = view.findViewById(R.id.ivEkycInfoItmRightIcon)
        }

        override fun onBind(data: UserInfoDisplay) {
            tvTitle.text = data.getTitle()
            edtValue.setText(data.getValue())
            ivIcon.setImageDrawable(data.getIcon())
        }
    }

    class UserInfoDisplay(val data: EkycInfo) {

        fun getTitle() = data.title

        fun getValue() = data.value

        fun getIcon(): Drawable? {
            return when (data.fieldType) {
                EkycInfo.FIELD_TYPE.EDIT -> getAppDrawable(R.drawable.fekyc_ic_edit)
                EkycInfo.FIELD_TYPE.TIME_PICKER -> getAppDrawable(R.drawable.fekyc_ic_calendar)
                EkycInfo.FIELD_TYPE.SELECT -> getAppDrawable(R.drawable.fekyc_ic_dropdrown)
                else -> null
            }
        }
    }

    interface IListener {
        fun onClickItem(item: UserInfo)
    }
}
