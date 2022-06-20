package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
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
        Manifest.permission.CAMERA
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
            override fun onLeftIconClick() {
                finish()
            }

            override fun onRightTextClick() {
            }
        })

        llSSN.setOnSafeClick {
            navigateToTakePictureScreen(UPLOAD_PHOTO_TYPE.FRONT)
        }

        llDriverLicense.setOnSafeClick {
            navigateToTakePictureScreen(UPLOAD_PHOTO_TYPE.BACK)
        }

        llPassport.setOnSafeClick {
            navigateToTakePictureScreen(UPLOAD_PHOTO_TYPE.FACE)
        }
    }

    private fun navigateToTakePictureScreen(ekycType: UPLOAD_PHOTO_TYPE) {
        doRequestPermission(permissionList, object : PermissionListener {
            override fun onAllow() {
                navigateTo(TakePictureActivity::class.java) {
                    it.putExtra(TakePictureActivity.SEND_EKYC_TYPE_KEY, ekycType)
                }
            }

            override fun onDenied() {

            }

            override fun onNeverAskAgain() {

            }
        })
    }
}
