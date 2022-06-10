package ai.ftech.ekyc.presentation.takepicture

import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import android.widget.ImageView
import androidx.activity.viewModels
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash

class TakePictureActivity : FEkycActivity(R.layout.fekyc_take_picture_activity) {
    private lateinit var cvCameraView: CameraView
    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivFlash: ImageView
    private lateinit var ivCapture: ImageView
    private lateinit var ivChangeCamera: ImageView
    private val viewModel by viewModels<TakePictureViewModel>()


    override fun onResume() {
        super.onResume()
        cvCameraView.open()
    }

    override fun onPause() {
        super.onPause()
        cvCameraView.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        cvCameraView.close()
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvTakePictureHeader)
        cvCameraView = findViewById(R.id.cvTakePictureCameraView)
        ivFlash = findViewById(R.id.ivTakePictureFlash)
        ivCapture = findViewById(R.id.ivTakePictureCapture)
        ivChangeCamera = findViewById(R.id.ivTakePictureChangeCamera)

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onRightIconClick() {

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
}
