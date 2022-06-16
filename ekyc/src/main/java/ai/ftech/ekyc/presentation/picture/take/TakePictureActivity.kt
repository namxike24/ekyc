package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.preview.PreviewPictureActivity
import ai.ftech.ekyc.utils.FileUtils
import android.widget.ImageView
import androidx.activity.viewModels
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import java.io.File

class TakePictureActivity : FEkycActivity(R.layout.fekyc_take_picture_activity) {
    companion object {
        const val SEND_EKYC_TYPE_KEY = "SEND_EKYC_TYPE_KEY"
    }

    private val viewModel by viewModels<TakePictureViewModel>()
    private lateinit var cvCameraView: CameraView
    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivFlash: ImageView
    private lateinit var ivCapture: ImageView
    private lateinit var ivChangeCamera: ImageView
    private var warningDialog: WarningCaptureDialog? = null
    private var isFrontFace = false
    private var isFlash = false

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
        viewModel.ekycType = intent.getSerializableExtra(SEND_EKYC_TYPE_KEY) as? EKYC_TYPE
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvTakePictureHeader)
        cvCameraView = findViewById(R.id.cvTakePictureCameraView)
        ivFlash = findViewById(R.id.ivTakePictureFlash)
        ivCapture = findViewById(R.id.ivTakePictureCapture)
        ivChangeCamera = findViewById(R.id.ivTakePictureChangeCamera)

        setFacing()

        tbvHeader.setTitle(viewModel.getToolbarTitleByEkycType())

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onRightIconClick() {
                warningDialog?.showDialog(supportFragmentManager, warningDialog!!::class.java.simpleName)
            }
        })

        cvCameraView.apply {
            setLifecycleOwner(this@TakePictureActivity)

            facing = if (isFrontFace) {
                Facing.FRONT
            } else {
                Facing.BACK
            }

            addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    uploadFile(result)
                }
            })
        }

        ivFlash.setOnSafeClick {
            if (isFlash) {
                cvCameraView.flash = Flash.OFF
                ivFlash.setImageDrawable(getAppDrawable(R.drawable.fekyc_ic_flash_off))
                isFlash = false
            } else {
                cvCameraView.flash = Flash.ON
                ivFlash.setImageDrawable(getAppDrawable(R.drawable.fekyc_ic_flash_on))
                isFlash = true
            }
        }

        ivCapture.setOnSafeClick {
//            navigateTo(TakePictureActivity::class.java) {
//                it.putExtra(EKYC_TYPE_KEY_SEND, EKYC_TYPE.SSN_BACK)
//            }

            cvCameraView.takePicture()
        }

        ivChangeCamera.setOnSafeClick {
            if (isFrontFace) {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            } else {
                cvCameraView.facing = Facing.FRONT
                isFrontFace = true
            }
        }
    }

    private fun uploadFile(result: PictureResult) {
        val path = viewModel.getFolderPathByEkycType()

        if (path != null) {
            val file = File(path)

            if (file.exists()) {
                FileUtils.deleteFile(path)
            }

            result.toFile(file) {
                if (it?.absolutePath != null) {
                    viewModel.uploadPhoto(it.absolutePath)
                }
            }
        }
    }

    private fun navigateToPreviewScreen(path: String) {
        navigateTo(PreviewPictureActivity::class.java) { intent ->
            intent.putExtra(PreviewPictureActivity.SEND_EKYC_TYPE_KEY, viewModel.ekycType)
            intent.putExtra(PreviewPictureActivity.SEND_PREVIEW_IMAGE_KEY, path)
        }
    }

    private fun setFacing() {
        when (viewModel.ekycType) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.PASSPORT_FRONT,

            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_BACK -> {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            }

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> {
                cvCameraView.facing = Facing.FRONT
                isFrontFace = true
            }

            else -> {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            }
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
