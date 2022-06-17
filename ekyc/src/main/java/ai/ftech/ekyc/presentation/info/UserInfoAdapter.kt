package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.adapter.BaseAdapter
import ai.ftech.dev.base.adapter.BaseVH
import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.UserInfo
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class UserInfoAdapter : BaseAdapter() {

    override fun getLayoutResource(viewType: Int) = R.layout.fekyc_user_info_item

    override fun getDataAtPosition(position: Int): UserInfoDisplay {
        return dataList[position] as UserInfoDisplay
    }

    override fun onCreateViewHolder(viewType: Int, view: View): BaseVH<*> {
        return UserInfoVH(view)
    }


    inner class UserInfoVH(view: View) : BaseVH<UserInfoDisplay>(view) {
        private var tvTitle: TextView
        private var edtValue: EditText
        private var ivIcon: ImageView

        init {
            tvTitle = view.findViewById(R.id.tvUserInfoItmTitle)
            edtValue = view.findViewById(R.id.tvUserInfoItmValue)
            ivIcon = view.findViewById(R.id.ivUserInfoItmRightIcon)
        }

        override fun onBind(data: UserInfoDisplay) {
            tvTitle.text = data.getTitle()
            edtValue.setText(data.getValue())
            ivIcon.setImageDrawable(data.getIcon())
        }
    }

    class UserInfoDisplay(val data: UserInfo) {

        fun getTitle() = data.title

        fun getValue() = data.value

        fun getIcon(): Drawable? {
            return when (data.fieldType) {
                UserInfo.FIELD_TYPE.EDIT -> getAppDrawable(R.drawable.fekyc_ic_edit)
                UserInfo.FIELD_TYPE.TIME_PICKER -> getAppDrawable(R.drawable.fekyc_ic_calendar)
                UserInfo.FIELD_TYPE.SELECT -> getAppDrawable(R.drawable.fekyc_ic_dropdrown)
                else -> null
            }
        }
    }
}
