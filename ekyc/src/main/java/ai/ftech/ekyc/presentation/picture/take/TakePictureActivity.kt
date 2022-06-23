package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.overlay.OverlayView
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_STATUS
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.confirm.ConfirmPictureActivity
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
    /**
     * view
     */
    private lateinit var ovFrameCrop: OverlayView
    private lateinit var cvCameraView: CameraView
    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivFlash: ImageView
    private lateinit var ivCapture: ImageView
    private lateinit var ivChangeCamera: ImageView

    /**
     * data
     */
    private val viewModel by viewModels<TakePictureViewModel>()
    private var isFrontFace = false
    private var isFlash = false
    private var file: File? = null

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
                showConfirmDialog()
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
            }

            override fun onError(exception: Exception) {
                Toast.makeText(this@TakePictureActivity, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        observer(viewModel.uploadPhoto) {
            when (it?.data) {
                UPLOAD_STATUS.FAIL -> {
                   handleCaseUploadFail(it.exception as APIException)
                }
                UPLOAD_STATUS.SUCCESS -> {
                    viewModel.clearUploadPhotoValue()
                    finish()
                    navigateToTakePictureScreen()
                }
                UPLOAD_STATUS.COMPLETE -> {
                    finish()
                    navigateTo(ConfirmPictureActivity::class.java)
                }
                UPLOAD_STATUS.NONE -> {}
                else -> {}
            }
        }
    }

    private fun handleCaseUploadFail(exp: APIException) {
        when (exp.code) {
            APIException.TIME_OUT_ERROR,
            APIException.NETWORK_ERROR -> showNotiNetworkDialog()
            else -> navigateToPreviewScreen(viewModel.filePath.value ?: "", exp.message)
        }
    }

    private fun uploadFile(result: PictureResult) {
        val path = viewModel.getFolderPathByEkycType()
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

    private fun navigateToTakePictureScreen() {
        navigateTo(TakePictureActivity::class.java)
    }

    private fun navigateToPreviewScreen(path: String, message: String? = null) {
        navigateTo(PreviewPictureActivity::class.java) { intent ->
            intent.putExtra(PreviewPictureActivity.SEND_PREVIEW_IMAGE_KEY, path)
            intent.putExtra(PreviewPictureActivity.SEND_MESSAGE_KEY, message)
        }
    }

    private fun setFacing() {
        when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FACE -> {
                cvCameraView.facing = Facing.FRONT
                isFrontFace = true
            }
            PHOTO_INFORMATION.BACK, PHOTO_INFORMATION.FRONT, PHOTO_INFORMATION.PAGE_NUMBER_2 -> {
                cvCameraView.facing = Facing.BACK
                isFrontFace = false
            }
        }
    }

    private fun getWarningType(): WARNING_TYPE {
        return when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FRONT,
            PHOTO_INFORMATION.BACK,
            PHOTO_INFORMATION.PAGE_NUMBER_2 -> {
                ovFrameCrop.apply {
                    setCropType(OverlayView.CROP_TYPE.REACTANGLE)

                }
                WARNING_TYPE.PAPERS
            }
            PHOTO_INFORMATION.FACE ->{
                ovFrameCrop.apply {
                    setCropType(OverlayView.CROP_TYPE.CIRCLE)
                }
                WARNING_TYPE.PORTRAIT
            }
        }
    }

    private fun getToolbarTitleByEkycType(): String {
        return when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FRONT -> getAppString(R.string.fekyc_take_picture_take_front)
            PHOTO_INFORMATION.BACK -> getAppString(R.string.fekyc_take_picture_take_back)
            PHOTO_INFORMATION.FACE -> getAppString(R.string.fekyc_take_picture_image_portrait)
            PHOTO_INFORMATION.PAGE_NUMBER_2 -> getAppString(R.string.fekyc_take_picture_take_passport)
        }
    }
}
