package ai.ftech.ekyc.common.widget.datepicker

import ai.ftech.base.common.BaseDialog
import ai.ftech.base.common.DialogScreen
import ai.ftech.base.extension.getAppColor
import ai.ftech.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import java.util.*

class DatePickerDialog(
    var title: String? = null,
    private var datePickerListener: ((Calendar) -> Unit)? = null,
    private var currentCalendar: Calendar? = null,
    var dateType: EkycFormInfo.DATE_TYPE? = null
) : BaseDialog(R.layout.fekyc_date_picker_dialog) {

    private lateinit var ivDatePickerDlgClose: AppCompatImageView
    private lateinit var tvDatePickerDlgTitle: AppCompatTextView
    private lateinit var tvDatePickerDlgSave: AppCompatTextView
    private lateinit var dpvDatePickerDlg: DatePickerView

    override fun getBackgroundId(): Int = R.id.constDatePickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
    }

    override fun onInitView() {
        val cal = Calendar.getInstance()
        ivDatePickerDlgClose = viewRoot.findViewById(R.id.ivDatePickerDlgClose)
        tvDatePickerDlgTitle = viewRoot.findViewById(R.id.tvDatePickerDlgTitle)
        dpvDatePickerDlg = viewRoot.findViewById(R.id.dpvDatePickerDlg)
        tvDatePickerDlgSave = viewRoot.findViewById(R.id.tvDatePickerDlgSave)

        dpvDatePickerDlg.apply {
            setLocale(DatePickerView.LOCALE.VI)
            currentCalendar?.let {
                setCurrentCalendar(it)
            }
            when (dateType) {
                EkycFormInfo.DATE_TYPE.FEATURE -> {
                    setMinYear(Calendar.getInstance().get(Calendar.YEAR))
                    setMaxYear(Calendar.getInstance().get(Calendar.YEAR) + 100)
                }
                EkycFormInfo.DATE_TYPE.PASS -> {
                    setMinYear(Calendar.getInstance().get(Calendar.YEAR) - 100)
                    setMaxYear(Calendar.getInstance().get(Calendar.YEAR))
                }
                else -> {}
            }
            listener = object : DatePickerView.Callback {
                override fun onDate(date: Date?) {
                    when (dateType) {
                        EkycFormInfo.DATE_TYPE.PASS -> {
                            if (date?.compareTo(cal.time)!! > 0) {
                                setEnableSaveButton(false)
                            } else {
                                setEnableSaveButton(true)
                            }
                        }
                        EkycFormInfo.DATE_TYPE.FEATURE -> {
                            if (date?.compareTo(cal.time)!! < 0) {
                                setEnableSaveButton(false)
                            } else {
                                setEnableSaveButton(true)
                            }
                        }
                        else -> {}
                    }

                }
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

    private fun setEnableSaveButton(isEnable: Boolean) {
        tvDatePickerDlgSave.isEnabled = isEnable
        tvDatePickerDlgSave.setTextColor(
            if (isEnable) getAppColor(R.color.fekyc_color_text_blue) else getAppColor(
                R.color.fekyc_color_text_hint
            )
        )
    }


    class Builder {
        private var title: String? = null
        private var datePickerListener: ((Calendar) -> Unit)? = null
        private var currentCalendar: Calendar? = null
        private var dateType: EkycFormInfo.DATE_TYPE? = null

        fun setDatePickerListener(listener: (Calendar) -> Unit) = apply {
            this.datePickerListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setCurrentCalendar(currentCalendar: Calendar?) = apply {
            this.currentCalendar = currentCalendar
        }

        fun setDateType(dateType: EkycFormInfo.DATE_TYPE?) = apply {
            this.dateType = dateType
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            DatePickerDialog(title, datePickerListener, currentCalendar, dateType).show(
                fragmentManager,
                null
            )
        }
    }
}

