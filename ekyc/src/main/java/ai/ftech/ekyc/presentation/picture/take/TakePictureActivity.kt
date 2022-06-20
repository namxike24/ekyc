package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.overlay.OverlayView
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.EKYC_PHOTO_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.preview.PreviewPictureActivity
import ai.ftech.ekyc.utils.FileUtils
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
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
    private lateinit var ovFrameCrop: OverlayView
    private lateinit var cvCameraView: CameraView
    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivFlash: ImageView
    private lateinit var ivCapture: ImageView
    private lateinit var ivChangeCamera: ImageView
    private var warningDialog: WarningCaptureDialog? = null
    private var isFrontFace = false
    private var isFlash = false
    private var file: File? = null


    override fun onResume() {
        super.onResume()
        cvCameraView.open()
        if (warningDialog == null) {
            val type = getWarningType()
            if (type != null) {
                warningDialog = WarningCaptureDialog(type)
            }
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
        viewModel.ekycType = intent.getSerializableExtra(SEND_EKYC_TYPE_KEY) as? EKYC_PHOTO_TYPE
    }

    override fun onInitView() {
        super.onInitView()
        ovFrameCrop = findViewById(R.id.ovTakePictureFrameCrop)
        tbvHeader = findViewById(R.id.tbvTakePictureHeader)
        cvCameraView = findViewById(R.id.cvTakePictureCameraView)
        ivFlash = findViewById(R.id.ivTakePictureFlash)
        ivCapture = findViewById(R.id.ivTakePictureCapture)
        ivChangeCamera = findViewById(R.id.ivTakePictureChangeCamera)

        setFacing()

        tbvHeader.setTitle(getToolbarTitleByEkycType())

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
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
            cvCameraView.takePictureSnapshot()
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

        ovFrameCrop.listener = object : OverlayView.ICallback {
            override fun onTakePicture(bitmap: Bitmap) {
                val file = FileUtils.bitmapToFile(bitmap, file?.absolutePath.toString())
                if (file != null) {
                    viewModel.uploadPhoto(file.absolutePath)
                }

//                navigateToPreviewScreen(file?.absolutePath!!)
            }

            override fun onError(exception: Exception) {
                Toast.makeText(this@TakePictureActivity, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadFile(result: PictureResult) {
        val path = viewModel.getFolderPathByEkycType()

        if (path != null) {
            val file = File(path)

            Log.d(TAG, "uploadFile: $path")

            if (file.exists()) {
                FileUtils.deleteFile(path)
            }

            result.toFile(file) {
                it?.let { file ->
                    this.file = file
                    ovFrameCrop.attachFile(file.absolutePath)
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
            EKYC_PHOTO_TYPE.SSN_FRONT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_PHOTO_TYPE.PASSPORT_FRONT,

            EKYC_PHOTO_TYPE.SSN_BACK,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            }

            EKYC_PHOTO_TYPE.SSN_PORTRAIT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT -> {
                cvCameraView.facing = Facing.FRONT
                isFrontFace = true
            }

            else -> {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            }
        }
    }

    private fun getWarningType(): WARNING_TYPE? {
        return when (viewModel.ekycType) {
            EKYC_PHOTO_TYPE.SSN_FRONT,
            EKYC_PHOTO_TYPE.SSN_BACK,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK,
            EKYC_PHOTO_TYPE.PASSPORT_FRONT -> WARNING_TYPE.PAPERS

            EKYC_PHOTO_TYPE.SSN_PORTRAIT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT,
            EKYC_PHOTO_TYPE.PORTRAIT -> WARNING_TYPE.PORTRAIT

            else -> null
        }
    }

    private fun getToolbarTitleByEkycType(): String {
        return when (viewModel.ekycType) {
            EKYC_PHOTO_TYPE.SSN_FRONT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_PHOTO_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_take_front)

            EKYC_PHOTO_TYPE.SSN_BACK,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_take_back)

            EKYC_PHOTO_TYPE.SSN_PORTRAIT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT -> getAppString(R.string.fekyc_take_picture_image_portrait)

            else -> AppConfig.EMPTY_CHAR
        }
    }
}
