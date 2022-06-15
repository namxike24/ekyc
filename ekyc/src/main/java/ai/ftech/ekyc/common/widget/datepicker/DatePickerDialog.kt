package ai.ftech.ekyc.common.widget.datepicker

import ai.ftech.dev.base.common.BaseDialog
import ai.ftech.dev.base.common.DialogScreen
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import java.util.*

class DatePickerDialog(
    var title: String? = null,
    var datePickerListener: ((Calendar) -> Unit)? = null,
    var currentCalendar: Calendar? = null
) : BaseDialog(R.layout.fekyc_date_picker_dialog) {

    lateinit var ivDatePickerDlgClose: AppCompatImageView
    lateinit var tvDatePickerDlgTitle: AppCompatTextView
    lateinit var tvDatePickerDlgSave: AppCompatTextView
    lateinit var dpvDatePickerDlg: DatePickerView

    override fun getBackgroundId(): Int = R.id.constDatePickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
    }

    override fun onInitView() {
        ivDatePickerDlgClose = viewRoot.findViewById(R.id.ivDatePickerDlgClose)
        tvDatePickerDlgTitle = viewRoot.findViewById(R.id.tvDatePickerDlgTitle)
        dpvDatePickerDlg = viewRoot.findViewById(R.id.dpvDatePickerDlg)
        tvDatePickerDlgSave = viewRoot.findViewById(R.id.tvDatePickerDlgSave)

        dpvDatePickerDlg.apply {
            setLocale(DatePickerView.LOCALE.VI)
            currentCalendar?.let {
                setCurrentCalendar(it)
            }
        }

        ivDatePickerDlgClose.setOnSafeClick {
            dismiss()
        }

        tvDatePickerDlgTitle.text = title

        tvDatePickerDlgSave.setOnSafeClick {
            dpvDatePickerDlg.apply {
                val calendar = getCalendarCurrent()
                calendar?.let {
                    datePickerListener?.invoke(calendar)
                }
                dismiss()
            }
        }
    }

    class Builder {
        private var title: String? = null
        private var datePickerListener: ((Calendar) -> Unit)? = null
        private var currentCalendar: Calendar? = null

        fun setDatePickerListener(listener: (Calendar) -> Unit) = apply {
            this.datePickerListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setCurrentCalendar(currentCalendar: Calendar) = apply {
            this.currentCalendar = currentCalendar
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            DatePickerDialog(title, datePickerListener, currentCalendar).show(fragmentManager, null)
        }
    }

}
