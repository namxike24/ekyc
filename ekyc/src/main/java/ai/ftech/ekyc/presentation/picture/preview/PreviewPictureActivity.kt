package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.take.TakePictureActivity
import android.widget.Button
import androidx.activity.viewModels

class PreviewPictureActivity : FEkycActivity(R.layout.fekyc_preview_picture_activity) {
    companion object {
        const val EKYC_TYPE_KEY_SEND = "EKYC_TYPE_KEY_SEND"
    }

    private val viewModel by viewModels<PreviewPictureViewModel>()
    private lateinit var tbvHeader: ToolbarView
    private lateinit var btnTakeAgain: Button
    private var warningDialog: WarningCaptureDialog? = null


    override fun onResume() {
        super.onResume()
        if (warningDialog == null) {
            warningDialog = WarningCaptureDialog(getWarningType())
        }
    }

    override fun onPause() {
        super.onPause()
        warningDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.ekycType = intent.getSerializableExtra(TakePictureActivity.EKYC_TYPE_KEY_SEND) as? EKYC_TYPE
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvPreviewPictureHeader)
        btnTakeAgain = findViewById(R.id.btnPreviewPictureTakeAgain)

        tbvHeader.setTitle(getToolbarTitle())

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onRightIconClick() {
                warningDialog?.showDialog(supportFragmentManager, warningDialog!!::class.java.simpleName)
            }
        })


        btnTakeAgain.setOnSafeClick {
            finish()
        }
    }

    private fun getToolbarTitle(): String {
        return when (viewModel.ekycType) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_image_front)

            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_image_back)

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> getAppString(R.string.fekyc_take_picture_image_portrait)

            else -> AppConfig.EMPTY_CHAR
        }
    }

    private fun getWarningType(): WARNING_TYPE {
        return when (viewModel.ekycType!!) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_BACK,
            EKYC_TYPE.PASSPORT_FRONT -> WARNING_TYPE.PAPERS

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> WARNING_TYPE.PORTRAIT
        }
    }
}
