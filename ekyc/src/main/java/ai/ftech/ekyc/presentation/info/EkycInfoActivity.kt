package ai.ftech.ekyc.presentation.info

import ai.ftech.base.extension.observer
import ai.ftech.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.action.FEkycActionResult
import ai.ftech.ekyc.common.getAppString
import ai.ftech.ekyc.common.widget.bottomsheetpickerdialog.BottomSheetPickerDialog
import ai.ftech.ekyc.common.widget.datepicker.DatePickerDialog
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.event.EkycEvent
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import ai.ftech.ekyc.utils.ShareFlowEventBus
import ai.ftech.ekyc.utils.TimeUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.*

class EkycInfoActivity : FEkycActivity(R.layout.fekyc_ekyc_info_activity) {
    /**
     * view
     */
    private lateinit var constRoot: ConstraintLayout
    private lateinit var tbvHeader: ToolbarView
    private lateinit var tvTypePapres: TextView
    private lateinit var rvUserInfo: RecyclerView
    private lateinit var btnCompleted: Button

    /**
     * data
     */
    private val viewModel by viewModels<EkycInfoViewModel>()

    private val adapter = FormInfoAdapter().apply {
        listener = object : FormInfoAdapter.IListener {
            override fun onClickItem(item: EkycFormInfo) {
                if (item.fieldType !== null) {
                    showBottomSheetDialog(item)
                }
            }
        }
    }

    override fun onInitView() {
        super.onInitView()
        constRoot = findViewById(R.id.constEkycInfoRoot)
        tbvHeader = findViewById(R.id.tbvEkycInfoHeader)
        tvTypePapres = findViewById(R.id.tvEkycInfoEkycTypeContent)
        rvUserInfo = findViewById(R.id.rvEkycInfoList)
        btnCompleted = findViewById(R.id.btnEkycInfoCompleted)

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                onBackPressed()
            }
        })

        btnCompleted.setOnSafeClick {
            val dataInfo =
                (adapter.dataList as List<FormInfoAdapter.FormInfoDisplay>).map { it.data }
            if (dataInfo.find { it.fieldValue.isNullOrEmpty() } != null) {
                showError(getAppString(R.string.empty_field_value))
            } else {
                showLoading()
                viewModel.submitInfo(dataInfo)
            }
        }

        rvUserInfo.layoutManager = LinearLayoutManager(this)
        rvUserInfo.adapter = adapter
        showLoading()
        viewModel.getEkycInfo()
        viewModel.getNationList()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        observer(viewModel.ekycInfo) {
            when (it?.resultStatus) {
                FEkycActionResult.RESULT_STATUS.SUCCESS -> {
                    tvTypePapres.text =
                        String.format("${it.data?.identityType}: ${it.data?.identityName}")
                    adapter.resetData(it.data?.form ?: emptyList())
                    hideLoading()
                }
                FEkycActionResult.RESULT_STATUS.ERROR -> {
                    hideLoading()
                }
                else -> {

                }
            }
        }

        observer(viewModel.submitInfo) {

            when (it?.resultStatus) {

                FEkycActionResult.RESULT_STATUS.SUCCESS -> {
                    hideLoading()
                    if (it.data == true) {
                        lifecycleScope.launch {

                            val event = EkycEvent().apply {
                                this.code = 0
                                this.message = "Ekyc thành công!"
                            }

                            ShareFlowEventBus.emitEvent(event)
                        }
                        finish()
                    }
                }

                FEkycActionResult.RESULT_STATUS.ERROR -> {
                    hideLoading()
                    lifecycleScope.launchWhenStarted {
                        val apiException = it.exception as APIException
                        if (apiException.code != APIException.EXPIRE_SESSION_ERROR) {
                            val event = EkycEvent().apply {
                                this.code = apiException.code
                                this.message = apiException.message.toString()
                            }
                            ShareFlowEventBus.emitEvent(event)
                            finish()
                        }
                    }
                }

                else -> {
                    Log.e(TAG, getAppString(R.string.fekyc_unknown_error))
                }
            }
        }
    }

    private fun showBottomSheetDialog(ekycInfo: EkycFormInfo) {
        when (ekycInfo.fieldType) {
            EkycFormInfo.FIELD_TYPE.DATE -> {
                showDatePickerDialog(ekycInfo)
            }
            EkycFormInfo.FIELD_TYPE.COUNTRY -> {
                showCityDialog(ekycInfo)
            }
            EkycFormInfo.FIELD_TYPE.NATIONAL -> {
                showNationDialog(ekycInfo)
            }
            EkycFormInfo.FIELD_TYPE.GENDER -> {
                showGenderDialog(ekycInfo)
            }
            else -> {}
        }
    }

    private fun showDatePickerDialog(ekycInfo: EkycFormInfo) {
        ekycInfo.fieldValue = "2000"
        DatePickerDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_ekyc_info_select_time))
            .setCurrentCalendar(
                try {
                    TimeUtils.getCalendarFromDateString(
                        ekycInfo.fieldValue ?: TimeUtils.dateToDateString(
                            Calendar.getInstance(),
                            TimeUtils.ISO_SHORT_DATE_FOMAT
                        ),
                        TimeUtils.ISO_SHORT_DATE_FOMAT
                    )
                } catch (e: Exception) {
                    TimeUtils.getCalendarFromDateString(
                        ekycInfo.fieldValue ?: TimeUtils.dateToDateString(
                            Calendar.getInstance(),
                            TimeUtils.ISO_SHORT_DATE_FOMAT
                        ),
                        TimeUtils.ISO_YEAR_FOMAT
                    )
                }
            )
            .setDateType(ekycInfo.dateType)
            .setDatePickerListener {
                val time = TimeUtils.dateToDateString(it, TimeUtils.ISO_SHORT_DATE_FOMAT)
                adapter.updateField(ekycInfo.id!!, time)
            }.show(supportFragmentManager)
    }

    private fun showCityDialog(ekycInfo: EkycFormInfo) {
        val list = viewModel.cityList.map { city ->
            BottomSheetPicker().apply {
                this.id = city.id.toString()
                this.title = city.name
                this.isSelected = (ekycInfo.fieldValue == city.name)
            }
        }

        BottomSheetPickerDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_ekyc_info_select_issued_by))
            .setListPicker(list)
            .setChooseItemListener {
                adapter.updateField(ekycInfo.id!!, it.title.toString())
            }
            .show(supportFragmentManager)
    }

    private fun showNationDialog(ekycInfo: EkycFormInfo) {
        val list = viewModel.nationList.map { nation ->
            BottomSheetPicker().apply {
                this.id = nation.id.toString()
                this.title = nation.name
                this.isSelected = (ekycInfo.fieldValue == nation.name)
            }
        }

        BottomSheetPickerDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_ekyc_info_select_issued_by))
            .setListPicker(list)
            .setChooseItemListener {
                adapter.updateField(ekycInfo.id!!, it.title.toString())
            }
            .show(supportFragmentManager)
    }

    private fun showGenderDialog(ekycInfo: EkycFormInfo) {
        val list = mutableListOf<BottomSheetPicker>()

        list.add(BottomSheetPicker().apply {
            this.id = ekycInfo.id.toString()
            this.title = getAppString(R.string.fekyc_ekyc_info_gender_male)
            this.isSelected =
                (ekycInfo.fieldValue == getAppString(R.string.fekyc_ekyc_info_gender_male))
        })

        list.add(BottomSheetPicker().apply {
            this.id = ekycInfo.id.toString()
            this.title = getAppString(R.string.fekyc_ekyc_info_gender_female)
            this.isSelected =
                (ekycInfo.fieldValue == getAppString(R.string.fekyc_ekyc_info_gender_female))
        })

        BottomSheetPickerDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_ekyc_info_select_gender))
            .setListPicker(list)
            .hasSearch(false)
            .setChooseItemListener {
                adapter.updateField(ekycInfo.id!!, it.title.toString())
            }
            .show(supportFragmentManager)
    }
}
