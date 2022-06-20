package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.adapter.BaseAdapter
import ai.ftech.dev.base.adapter.BaseVH
import ai.ftech.dev.base.extension.*
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.ekyc.FormInfo
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class FormInfoAdapter : BaseAdapter() {

    var listener: IListener? = null

    override fun getLayoutResource(viewType: Int) = R.layout.fekyc_ekyc_info_item

    override fun getDataAtPosition(position: Int): FormInfoDisplay {
        return dataList[position] as FormInfoDisplay
    }

    override fun onCreateViewHolder(viewType: Int, view: View): BaseVH<*> {
        return FormInfoVH(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun resetData(dataList: List<Any>) {
        val list = dataList.map {
            FormInfoDisplay(it as FormInfo)
        }
        this.dataList.clear()
        this.dataList.addAll(list)
        notifyDataSetChanged()
    }

    inner class FormInfoVH(view: View) : BaseVH<FormInfoDisplay>(view) {
        private var tvTitle: TextView
        private var edtValue: EditText
        private var ivIcon: ImageView
        private val FIRST_POSITION = 0
        private val LAST_POSITION = dataList.size - 1

        init {
            tvTitle = view.findViewById(R.id.tvEkycInfoItmTitle)
            edtValue = view.findViewById(R.id.tvEkycInfoItmValue)
            ivIcon = view.findViewById(R.id.ivEkycInfoItmRightIcon)


            ivIcon.setOnSafeClick {
                val item = getDataAtPosition(adapterPosition).data
                listener?.onClickItem(item)
            }
        }

        override fun onBind(data: FormInfoDisplay) {
            tvTitle.text = data.getTitle()
            edtValue.setText(data.getValue())
            ivIcon.setImageDrawable(data.getIcon())

            checkStateFormHasEditable(data)
        }

        private fun checkStateFormHasEditable(data: FormInfoDisplay) {
            if (data.isEditable()) {
                ivIcon.show()
                when (data.getFieldType()) {
                    FormInfo.FIELD_TYPE.STRING,
                    FormInfo.FIELD_TYPE.NUMBER,
                    FormInfo.FIELD_TYPE.COUNTRY -> {
                        setEnableEditText(true)
                    }

                    FormInfo.FIELD_TYPE.DATE,
                    FormInfo.FIELD_TYPE.GENDER,
                    FormInfo.FIELD_TYPE.NATIONAL -> {
                        setEnableEditText(false)
                    }

                    else -> setEnableEditText(false)
                }
            } else {
                ivIcon.gone()
                setEnableEditText(false)
            }
        }

        private fun setEnableEditText(isEnable: Boolean) {
            edtValue.isFocusable = isEnable
            edtValue.isFocusableInTouchMode = isEnable
        }
    }

    class FormInfoDisplay(val data: FormInfo) {

        fun getTitle() = data.title

        fun getValue() = data.value

        fun getIcon(): Drawable? {
            return when (getFieldType()) {
                FormInfo.FIELD_TYPE.STRING,
                FormInfo.FIELD_TYPE.NUMBER,
                FormInfo.FIELD_TYPE.COUNTRY -> getAppDrawable(R.drawable.fekyc_ic_edit)

                FormInfo.FIELD_TYPE.DATE -> getAppDrawable(R.drawable.fekyc_ic_calendar)

                FormInfo.FIELD_TYPE.GENDER,
                FormInfo.FIELD_TYPE.NATIONAL -> getAppDrawable(R.drawable.fekyc_ic_dropdrown)

                else -> null
            }
        }

        fun getFieldType(): FormInfo.FIELD_TYPE? {
            return data.fieldType
        }

        fun getCorner() = getAppDimension(ai.ftech.dev.base.R.dimen.fbase_corner_10)

        fun isEditable() = data.isEditable
    }

    interface IListener {
        fun onClickItem(item: FormInfo)
    }
}
