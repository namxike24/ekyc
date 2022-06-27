package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.extension.observer
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
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

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

    override fun onStart() {
        super.onStart()
        addTouchRootListener(constRoot, object : ITouchOutsideViewListener {
            override fun onTouchOutside(view: View, event: MotionEvent) {
                hideKeyboard()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        removeTouchRootListener()
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

        btnCompleted.setOnClickListener {
//            showLoading()
            viewModel.submitInfo(adapter.dataList as MutableList<EkycFormInfo>)
        }

        rvUserInfo.layoutManager = LinearLayoutManager(this)
        rvUserInfo.adapter = adapter

        viewModel.getEkycInfo()
        viewModel.getCityList()
        viewModel.getNationList()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        observer(viewModel.ekycInfo) {
            tvTypePapres.text = String.format("${it?.identityType}: ${it?.identityName}")
            adapter.resetData(it?.formList ?: emptyList())
        }

        observer(viewModel.submitInfo) {
            hideLoading()
            when (it?.resultStatus) {
                FEkycActionResult.RESULT_STATUS.SUCCESS -> {
                    if (it.data == true) {
                        lifecycleScope.launch {
                            val event = EkycEvent().apply {
                                this.message = "Ekyc thành công!!"
                            }
                            ShareFlowEventBus.emitEvent(event)
                        }
                        finish()
                    }
                }
                FEkycActionResult.RESULT_STATUS.ERROR -> {
                    when ((it.exception as APIException).code) {
                        APIException.EXPIRE_SESSION_ERROR -> {
                            lifecycleScope.launchWhenStarted {
                                val event = EkycEvent().apply {
                                    this.throwable = it.exception
                                }
                                ShareFlowEventBus.emitEvent(event)
                            }
                            finish()
                        }
                    }
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
        }
    }

    private fun showDatePickerDialog(ekycInfo: EkycFormInfo) {
        DatePickerDialog.Builder()
            .setTitle(getAppString(R.string.fekyc_ekyc_info_select_time))
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
                this.isSelected = (ekycInfo.value == city.name)
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
                this.isSelected = (ekycInfo.value == nation.name)
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
            this.isSelected = (ekycInfo.value == getAppString(R.string.fekyc_ekyc_info_gender_male))
        })

        list.add(BottomSheetPicker().apply {
            this.id = ekycInfo.id.toString()
            this.title = getAppString(R.string.fekyc_ekyc_info_gender_female)
            this.isSelected = (ekycInfo.value == getAppString(R.string.fekyc_ekyc_info_gender_female))
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
