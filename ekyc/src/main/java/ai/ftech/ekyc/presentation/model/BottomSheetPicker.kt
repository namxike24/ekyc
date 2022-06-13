package ai.ftech.ekyc.presentation.model

import ai.ftech.ekyc.R

class BottomSheetPicker {
    var id: String? = null
    var title: String? = null
    var optionalData : Any? = null
    var isSelected : Boolean = false


    fun getTextColor(): Int {
        return if (isSelected) {
            R.color.fekyc_color_text_selected
        } else {
            R.color.fekyc_color_text_black
        }
    }

}
