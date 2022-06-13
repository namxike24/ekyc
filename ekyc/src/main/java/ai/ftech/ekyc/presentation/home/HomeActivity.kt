package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.bottomsheetpickerdialog.BottomSheetPickerDialog
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.presentation.model.BottomSheetPicker
import ai.ftech.ekyc.presentation.takepicture.TakePictureActivity
import android.Manifest
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : FEkycActivity(R.layout.fekyc_home_activity) {
    private lateinit var tbvHeader: ToolbarView
    private lateinit var rvListStep: RecyclerView
    private lateinit var llSSN: LinearLayout
    private lateinit var llDriverLicense: LinearLayout
    private lateinit var llPassport: LinearLayout
    private val adapter by lazy {
        StepIdentityAdapter()
    }
    private val permissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvHomeHeader)
        rvListStep = findViewById(R.id.rvHomeListStep)
        llSSN = findViewById(R.id.llHomeSSN)
        llDriverLicense = findViewById(R.id.llHomeDriverLicense)
        llPassport = findViewById(R.id.llHomePassport)


        rvListStep.layoutManager = LinearLayoutManager(this)
        rvListStep.adapter = adapter

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onRightTextClick() {

            }
        })

        llSSN.setOnSafeClick {
            doRequestPermission(permissionList, object : PermissionListener {
                override fun onAllow() {
                    navigateTo(TakePictureActivity::class.java)
                }

                override fun onDenied() {

                }

                override fun onNeverAskAgain() {

                }
            })
        }

        llDriverLicense.setOnSafeClick {

        }

        llPassport.setOnSafeClick {
            BottomSheetPickerDialog.Builder()
                .setTitle(getAppString(R.string.fekyc_home_select_type_of_papers_passport))
                .setListPicker(arrayListOf(
                    BottomSheetPicker().apply {
                        id = "1"
                        title = "1"
                    },
                    BottomSheetPicker().apply {
                        id = "1"
                        title = "2"
                    },
                    BottomSheetPicker().apply {
                        id = "12"
                        title = "3"
                    },
                    BottomSheetPicker().apply {
                        id = "19"
                        title = "4"
                    },
                    BottomSheetPicker().apply {
                        id = "18"
                        title = "5"
                    },
                    BottomSheetPicker().apply {
                        id = "16"
                        title = "6"
                    },
                    BottomSheetPicker().apply {
                        id = "14"
                        title = "vlalva"
                    },
                    BottomSheetPicker().apply {
                        id = "11"
                        title = "vlalva"
                    },
                    BottomSheetPicker().apply {
                        id = "13"
                        title = "vlalva"
                    }
                ))
                .setChooseItemListener {

                }
                .setVisibleItem(4)
                .show(supportFragmentManager)
        }
    }
}
