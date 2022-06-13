package ai.ftech.ekyc.common.widget.bottomsheetpickerdialog

import ai.ftech.dev.base.common.BaseDialog
import ai.ftech.dev.base.common.DialogScreen
import ai.ftech.dev.base.extension.getAppDimensionPixel
import ai.ftech.dev.base.extension.show
import ai.ftech.ekyc.R
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import androidx.fragment.app.FragmentManager

class BottomSheetPickerDialog private constructor(
    var lstPickers: List<BottomSheetPicker> = listOf(),
    var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null,
    var title: String? = null,
    var hint: String? = null,
    var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT,
    var visibleItem: Int? = null
) : BaseDialog(R.layout.fekyc_bottom_sheet_picker_dialog) {

    companion object {
        const val DEFAULT_RATIO_DIALOG_HEIGHT = 0.5f
    }

    override fun getBackgroundId(): Int = R.id.constBottomSheetPickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
    }

    override fun onInitView() {
        with(binding) {
            abBottomSheetPickerDlg.apply {
                setOnIconLeftClickListener {
                    dismissDialog()
                }
                setLeftLabel(title)
            }

            if (!hint.isNullOrEmpty()) {
                tvBottomSheetPickerDlgHint.show()
                tvBottomSheetPickerDlgHint.text = hint
            }

            constBottomSheetPickerDlgRoot.maxHeight = (requireActivity().getScreenHeight() * ratioDialogHeight).toInt()

            cvBottomSheetPickerDlg.apply {
                setAdapter(BottomSheetPickerAdapter().apply {
                    listener = object : BottomSheetPickerAdapter.BottomSheetItemListener {
                        override fun onClickItem(bottomSheetPicker: BottomSheetPicker) {
                            chooseItemListener?.invoke(bottomSheetPicker)
                            dismissDialog()
                        }
                    }
                })
                setItemDecoration(SpaceItemDecoration(getAppDimensionPixel(R.dimen.fbase_dimen_20)))
                setMaximumVisibleItem(visibleItem, visibleItem?.plus(1))
                addItems(lstPickers.toMutableList())
            }
        }
    }

    class Builder {
        private var lstPickers: List<BottomSheetPicker> = listOf()
        private var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null
        private var title: String? = null
        private var hint: String? = null
        private var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT
        private var visibleItem: Int? = null

        fun setListPicker(lstPickers: List<BottomSheetPicker>) = apply {
            this.lstPickers = lstPickers
        }

        fun setChooseItemListener(listener: (BottomSheetPicker) -> Unit) = apply {
            this.chooseItemListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setHint(hint: String?) = apply {
            this.hint = hint
        }

        fun setRatioDialogHeight(ratio: Float) = apply {
            this.ratioDialogHeight = ratio
        }

        fun setVisibleItem(limit: Int) = apply {
            this.visibleItem = limit
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            BottomSheetPickerDialog(lstPickers, chooseItemListener, title, hint, ratioDialogHeight, visibleItem).show(fragmentManager, null)
        }
    }

}
