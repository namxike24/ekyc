package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.presentation.picture.take.TakePictureActivity
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
            navigateToTakePictureScreen(EKYC_TYPE.SSN_FRONT)
        }

        llDriverLicense.setOnSafeClick {
            navigateToTakePictureScreen(EKYC_TYPE.SSN_BACK)
        }

        llPassport.setOnSafeClick {
            navigateToTakePictureScreen(EKYC_TYPE.SSN_PORTRAIT)
        }
    }

    private fun navigateToTakePictureScreen(ekycType: EKYC_TYPE) {
        doRequestPermission(permissionList, object : PermissionListener {
            override fun onAllow() {
                navigateTo(TakePictureActivity::class.java) {
                    it.putExtra(TakePictureActivity.EKYC_TYPE_KEY_SEND, ekycType)
                }
            }

            override fun onDenied() {

            }

            override fun onNeverAskAgain() {

            }
        })
    }
}
