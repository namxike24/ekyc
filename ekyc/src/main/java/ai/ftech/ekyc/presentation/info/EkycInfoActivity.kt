package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.bottomsheetpickerdialog.BottomSheetPickerDialog
import ai.ftech.ekyc.common.widget.datepicker.DatePickerDialog
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
                showConfirmDialog()
            }
        })

        btnCompleted.setOnClickListener {
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
            Log.d(TAG, "onObserverViewModel: $it")
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
                adapter.updateField(ekycInfo.id!!, "")
            }.show(supportFragmentManager)
    }

    private fun showCityDialog(ekycInfo: EkycFormInfo) {
        val list = viewModel.cityList.mapIndexed { index, city ->
            BottomSheetPicker().apply {
                this.id = city.id.toString()
                this.title = city.name
                this.isSelected = (index == 0)
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
        val list = viewModel.nationList.mapIndexed { index, city ->
            BottomSheetPicker().apply {
                this.id = city.id.toString()
                this.title = city.name
                this.isSelected = (index == 0)
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
            this.id = "1"
            this.title = getAppString(R.string.fekyc_ekyc_info_gender_male)
            this.isSelected = true
        })

        list.add(BottomSheetPicker().apply {
            this.id = "2"
            this.title = getAppString(R.string.fekyc_ekyc_info_gender_female)
            this.isSelected = false
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
