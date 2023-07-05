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
            setOnLeftIconClick {
                changeFlash()
                if (isFlashOn()) {
                    setLeftIcon(R.drawable.fekyc_ic_flash_off)
                } else {
                    setLeftIcon(R.drawable.fekyc_ic_flash_on)
                }
            }
            setOnRightIconClick {
                changeFacing()
            }
            setOnMidIconClick {
                capture(object : CaptureView.ICallback {
                    override fun onCaptureSuccess(file: File) {
                        ivPreview.show()
                        ImageLoaderFactory.glide()
                            .loadImage(this@TestCaptureActivity, file.absolutePath, ivPreview, true)
                    }

                    override fun onCaptureFail(exception: Exception) {
                        Toast.makeText(this@TestCaptureActivity, "Fail", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onBackPressed() {
        ivPreview.gone()
    }
}
