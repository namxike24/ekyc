package ai.ftech.ekyc.common.widget.bottomsheetpickerdialog

import ai.ftech.dev.base.adapter.BaseAdapter
import ai.ftech.dev.base.adapter.BaseVH
import ai.ftech.dev.base.extension.getAppColor
import ai.ftech.ekyc.R
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat

class BottomSheetPickerAdapter : BaseAdapter() {
    var listener: BottomSheetItemListener? = null

    override fun getLayoutResource(viewType: Int) = R.layout.fekyc_bottom_sheet_picker_item

    override fun onCreateViewHolder(viewType: Int, view: View): BaseVH<*> =
        SchoolYearViewHolder(view)

    override fun getDataAtPosition(position: Int) = dataList[position] as BottomSheetPicker

    inner class SchoolYearViewHolder(view: View) : BaseVH<BottomSheetPicker>(view) {
        var llBottomSheetPickerItm: LinearLayoutCompat
        var tvBottomSheetPickerItm: AppCompatTextView

        init {
            tvBottomSheetPickerItm = view.findViewById(R.id.tvBottomSheetPickerItm)
            llBottomSheetPickerItm = view.findViewById(R.id.llBottomSheetPickerItm)
            llBottomSheetPickerItm.setOnClickListener {
                listener?.onClickItem(dataList[adapterPosition] as BottomSheetPicker)
            }
        }

        override fun onBind(data: BottomSheetPicker) {
            tvBottomSheetPickerItm.text = data.title
            tvBottomSheetPickerItm.setTextColor(getAppColor(data.getTextColor()))
        }
    }

    interface BottomSheetItemListener {
        fun onClickItem(bottomSheetPicker: BottomSheetPicker)
    }
}
