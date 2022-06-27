package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.event.EkycEvent
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.presentation.picture.take.EkycStep
import ai.ftech.ekyc.presentation.picture.take.TakePictureActivity
import ai.ftech.ekyc.publish.FTechEkycInfo
import ai.ftech.ekyc.utils.ShareFlowEventBus
import android.Manifest
import android.content.Intent
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

class HomeActivity : FEkycActivity(R.layout.fekyc_home_activity) {
    companion object {
        const val SEND_RESULT_FTECH_EKYC_INFO = "SEND_RESULT_FTECH_EKYC_INFO"
    }

    /**
     * view
     */
    private lateinit var tbvHeader: ToolbarView
    private lateinit var rvListStep: RecyclerView
    private lateinit var llSSN: LinearLayout
    private lateinit var llDriverLicense: LinearLayout
    private lateinit var llPassport: LinearLayout
    private val adapter = StepIdentityAdapter()

    /**
     * data
     */
    private val permissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onResume() {
        super.onResume()
        EkycStep.clear()
    }

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
                onBackPressed()
            }

            override fun onRightTextClick() {
            }
        })

        llSSN.setOnSafeClick {
            navigateToTakePictureScreen(PHOTO_TYPE.SSN)
        }

        llDriverLicense.setOnSafeClick {
            navigateToTakePictureScreen(PHOTO_TYPE.DRIVER_LICENSE)
        }

        llPassport.setOnSafeClick {
            navigateToTakePictureScreen(PHOTO_TYPE.PASSPORT)
        }

        lifecycleScope.launchWhenResumed {
            val flow = ShareFlowEventBus.events.filter {
                it is EkycEvent
            }

            flow.collectLatest {
                val info = FTechEkycInfo().apply {
                    if (it is EkycEvent) {
                        if (it.exception == null) {
                            this.message = it.message
                        } else {
                            this.code = (it as? EkycEvent)?.exception?.code ?: -1
                            this.message = (it as? EkycEvent)?.exception?.message.toString()
                        }
                    }
                }
                val intent = Intent()
                intent.putExtra(SEND_RESULT_FTECH_EKYC_INFO, info)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun navigateToTakePictureScreen(photoType: PHOTO_TYPE) {
        doRequestPermission(permissionList, object : PermissionListener {
            override fun onAllow() {
                EkycStep.setType(photoType)
                navigateTo(TakePictureActivity::class.java)
            }

            override fun onDenied() {

            }

            override fun onNeverAskAgain() {

            }
        })
    }
}
