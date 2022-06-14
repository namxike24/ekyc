package ai.ftech.ekyc.common.widget.bottomsheetpickerdialog

import ai.ftech.dev.base.common.BaseDialog
import ai.ftech.dev.base.common.DialogScreen
import ai.ftech.dev.base.extension.getScreenHeight
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.widget.recyclerview.CollectionView
import ai.ftech.ekyc.common.widget.recyclerview.DividerDecorator
import ai.ftech.ekyc.common.widget.searchview.SearchView
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import java.util.*

class BottomSheetPickerDialog private constructor(
    var lstPickers: List<BottomSheetPicker> = listOf(),
    var chooseItemListener: ((BottomSheetPicker) -> Unit)? = null,
    var title: String? = null,
    var ratioDialogHeight: Float = DEFAULT_RATIO_DIALOG_HEIGHT,
    var visibleItem: Int? = null
) : BaseDialog(R.layout.fekyc_bottom_sheet_picker_dialog) {

    lateinit var constBottomSheetPickerDlgRoot: ConstraintLayout
    lateinit var cvBottomSheetPickerDlg: CollectionView
    lateinit var tvBottomSheetPickerTitle: AppCompatTextView
    lateinit var svBottomSheetPickerDialog: SearchView


    companion object {
        const val DEFAULT_RATIO_DIALOG_HEIGHT = 0.5f
    }

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

        tvBottomSheetPickerTitle.text = title

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
            setItemDecoration(DividerDecorator(context, R.drawable.fekyc_shape_divider, false, true))
            setMaximumVisibleItem(visibleItem, visibleItem?.plus(1))
            addItems(lstPickers.toMutableList())
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

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            BottomSheetPickerDialog(lstPickers, chooseItemListener, title, ratioDialogHeight, visibleItem).show(fragmentManager, null)
        }
    }

}
