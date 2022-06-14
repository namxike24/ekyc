package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.preview.PreviewPictureActivity
import android.widget.ImageView
import androidx.activity.viewModels
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash

class TakePictureActivity : FEkycActivity(R.layout.fekyc_take_picture_activity) {
    companion object {
        const val EKYC_TYPE_KEY_SEND = "EKYC_TYPE_KEY_SEND"
    }

    private val viewModel by viewModels<TakePictureViewModel>()
    private lateinit var cvCameraView: CameraView
    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivFlash: ImageView
    private lateinit var ivCapture: ImageView
    private lateinit var ivChangeCamera: ImageView
    private var warningDialog: WarningCaptureDialog? = null

    override fun onResume() {
        super.onResume()
        cvCameraView.open()
        if (warningDialog == null) {
            warningDialog = WarningCaptureDialog(getWarningType())
        }
    }

    override fun onPause() {
        super.onPause()
        cvCameraView.close()
        warningDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        cvCameraView.close()
        warningDialog = null
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.ekycType = intent.getSerializableExtra(EKYC_TYPE_KEY_SEND) as? EKYC_TYPE
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvTakePictureHeader)
        cvCameraView = findViewById(R.id.cvTakePictureCameraView)
        ivFlash = findViewById(R.id.ivTakePictureFlash)
        ivCapture = findViewById(R.id.ivTakePictureCapture)
        ivChangeCamera = findViewById(R.id.ivTakePictureChangeCamera)



        tbvHeader.setTitle(getToolbarTitle())

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onRightIconClick() {
                warningDialog?.showDialog(supportFragmentManager, warningDialog!!::class.java.simpleName)
            }
        })

        cvCameraView.setLifecycleOwner(this)
        cvCameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {

            }
        })

        ivFlash.setOnSafeClick {
            if (viewModel.isFlash) {
                cvCameraView.flash = Flash.OFF
                ivFlash.setImageDrawable(getAppDrawable(R.drawable.fekyc_ic_flash_off))
                viewModel.isFlash = false
            } else {
                cvCameraView.flash = Flash.ON
                ivFlash.setImageDrawable(getAppDrawable(R.drawable.fekyc_ic_flash_on))
                viewModel.isFlash = true
            }
        }

        ivCapture.setOnSafeClick {
//            navigateTo(TakePictureActivity::class.java) {
//                it.putExtra(EKYC_TYPE_KEY_SEND, EKYC_TYPE.SSN_BACK)
//            }
            navigateTo(PreviewPictureActivity::class.java) {
                it.putExtra(PreviewPictureActivity.EKYC_TYPE_KEY_SEND, viewModel.ekycType)
            }
        }

        ivChangeCamera.setOnSafeClick {
            if (viewModel.isFrontFace) {
                cvCameraView.facing = Facing.BACK
                viewModel.isFrontFace = false
            } else {
                cvCameraView.facing = Facing.FRONT
                viewModel.isFrontFace = true
            }
        }
    }

    private fun getToolbarTitle(): String {
        return when (viewModel.ekycType) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_take_front)

            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_take_back)

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
