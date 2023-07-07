package ai.ftech.fekyc.presentation.picture.take

import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.extension.gone
import ai.ftech.fekyc.base.extension.show
import ai.ftech.fekyc.common.imageloader.ImageLoaderFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class TestCaptureActivity : AppCompatActivity() {
    private lateinit var cvCaptureView: CaptureView
    private lateinit var ivPreview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_capture_activity)
        cvCaptureView = findViewById(R.id.cvTestCaptureCaptureView)
        ivPreview = findViewById(R.id.ivTestCapturePreview)

        cvCaptureView.apply {
            setLifecycleOwner(this@TestCaptureActivity)
            setOnFlashIconClick {
                Toast.makeText(this@TestCaptureActivity, "Flash", Toast.LENGTH_SHORT).show()
            }
            setOnFacingIconClick {
                Toast.makeText(this@TestCaptureActivity, "Facing", Toast.LENGTH_SHORT).show()
            }
            setOnTakePictureIconClick {
                Toast.makeText(this@TestCaptureActivity, "Take picture", Toast.LENGTH_SHORT).show()
            }
            setOnTakePictureCallback(object : CaptureView.ICallback {
                override fun onTakePictureSuccess(file: File) {
                    ivPreview.show()
                    ImageLoaderFactory.glide().loadImage(this@TestCaptureActivity, file.absolutePath, ivPreview, true)
                }

                override fun onTakePictureFail(exception: Exception) {
                    Toast.makeText(this@TestCaptureActivity, exception.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onBackPressed() {
        ivPreview.gone()
    }
}
