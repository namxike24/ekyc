package ai.ftech.fekyc.common.widget.bottomsheetpickerdialog

import ai.ftech.fekyc.base.common.BaseDialog
import ai.ftech.fekyc.base.common.DialogScreen
import ai.ftech.fekyc.base.extension.getScreenHeight
import ai.ftech.fekyc.base.extension.gone
import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.base.extension.show
import ai.ftech.fekyc.R
import ai.ftech.fekyc.common.widget.recyclerview.CollectionView
import ai.ftech.fekyc.common.widget.recyclerview.DividerDecorator
import ai.ftech.fekyc.common.widget.searchview.SearchView
import ai.ftech.fekyc.presentation.model.BottomSheetPicker
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager

class BottomSheetPickerDialog private constructor(
    private var lstPickers: List<BottomSheetPicker> = listOf(),
    var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null,
    var title: String? = null,
    private var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT,
    private var visibleItem: Int? = null,
    private var hasSearch: Boolean = true
) : BaseDialog(R.layout.fekyc_bottom_sheet_picker_dialog) {
    companion object {
        const val DEFAULT_RATIO_DIALOG_HEIGHT = 0.5f
    }

    private lateinit var constBottomSheetPickerDlgRoot: ConstraintLayout
    private lateinit var cvBottomSheetPickerDlg: CollectionView
    private lateinit var tvBottomSheetPickerTitle: AppCompatTextView
    private lateinit var svBottomSheetPickerDialog: SearchView
    private lateinit var ivBottomSheetPickerClose: AppCompatImageView

    override fun getBackgroundId(): Int = R.id.constBottomSheetPickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
    }

    override fun onInitView() {
        constBottomSheetPickerDlgRoot = viewRoot.findViewById(R.id.constBottomSheetPickerDlgRoot)
        cvBottomSheetPickerDlg = viewRoot.findViewById(R.id.cvBottomSheetPickerDlg)
        tvBottomSheetPickerTitle = viewRoot.findViewById(R.id.tvBottomSheetPickerTitle)
        svBottomSheetPickerDialog = viewRoot.findViewById(R.id.svBottomSheetPickerDialog)
        ivBottomSheetPickerClose = viewRoot.findViewById(R.id.ivBottomSheetPickerClose)

        ivBottomSheetPickerClose.setOnSafeClick {
            dismiss()
        }

        tvBottomSheetPickerTitle.text = title

        constBottomSheetPickerDlgRoot.maxHeight =
            (requireActivity().getScreenHeight() * ratioDialogHeight).toInt()

        cvBottomSheetPickerDlg.apply {
            setAdapter(BottomSheetPickerAdapter().apply {
                listener = object : BottomSheetPickerAdapter.BottomSheetItemListener {
                    override fun onClickItem(bottomSheetPicker: BottomSheetPicker) {
                        chooseItemListener?.invoke(bottomSheetPicker)
                        dismissDialog()
                    }
                }
            })
            setItemDecoration(
                DividerDecorator(
                    context,
                    R.drawable.fekyc_shape_divider,
                    isShowLast = false,
                    isDrawOver = true
                )
            )
            setMaximumVisibleItem(visibleItem, visibleItem?.plus(1))
            addItems(lstPickers.toMutableList())
        }

        if (hasSearch) {
            svBottomSheetPickerDialog.show()
        } else {
            svBottomSheetPickerDialog.gone()
        }

        svBottomSheetPickerDialog.apply {
            setTextChangeListener { value ->
                cvBottomSheetPickerDlg.resetData(lstPickers.filter {
                    it.title?.lowercase()?.contains(value) ?: false
                })
            }
            setOnClearListener {
                cvBottomSheetPickerDlg.resetData(lstPickers.toMutableList())
            }
            setSubmitListener { value ->
                cvBottomSheetPickerDlg.resetData(lstPickers.filter {
                    it.title?.lowercase()?.contains(value) ?: false
                })
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
        private var hasSearch: Boolean = true

        fun setListPicker(lstPickers: List<BottomSheetPicker>) = apply {
            this.lstPickers = lstPickers
        }

        fun setChooseItemListener(listener: (BottomSheetPicker) -> Unit) = apply {
            this.chooseItemListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setRatioDialogHeight(ratio: Float) = apply {
            this.ratioDialogHeight = ratio
        }

        fun setVisibleItem(limit: Int) = apply {
            this.visibleItem = limit
        }

        fun hasSearch(isSearch: Boolean) = apply {
            this.hasSearch = isSearch
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            BottomSheetPickerDialog(
                lstPickers,
                chooseItemListener,
                title,
                ratioDialogHeight,
                visibleItem,
                hasSearch
            ).show(fragmentManager, null)
        }
    }
}
