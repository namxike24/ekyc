package ai.ftech.fekyc.presentation.picture.take

import ai.ftech.fekyc.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        findViewById<CaptureView>(R.id.cvCaptureView).apply {
            setLifecycleOwner(this@TestActivity)
            setOnLeftIconClick {
                changeFlash()
                if (isFlashOn()) {
                    setLeftIcon(R.drawable.fekyc_ic_flash_off)
                } else {
                    setLeftIcon(R.drawable.fekyc_ic_flash_on)
                }
            }
        }
    }
}
