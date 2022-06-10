package ai.ftech.ekyc.presentation.takepicture

import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult

class TakePictureActivity : FEkycActivity(R.layout.fekyc_take_picture_activity) {
    private lateinit var cvCameraView : CameraView

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
        cvCameraView= findViewById(R.id.cvTakePictureCameraView)


        cvCameraView.setLifecycleOwner(this)

        cvCameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {

            }
        })
    }
}
