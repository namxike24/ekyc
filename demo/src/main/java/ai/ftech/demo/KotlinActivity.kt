package ai.ftech.demo

import ai.ftech.ekyc.publish.FTechEkycInfo
import ai.ftech.ekyc.publish.FTechEkycManager
import ai.ftech.ekyc.publish.IFTechEkycCallback
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KotlinActivity : AppCompatActivity() {
    private lateinit var tvState: TextView
    private lateinit var btnEkyc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity)

        tvState = findViewById(R.id.tvDemoState)
        btnEkyc = findViewById(R.id.btnDemoEkyc)

        tvState.setOnClickListener {
            tvState.text = ""
        }

        btnEkyc.setOnClickListener {
            FTechEkycManager.startEkyc("123", "111", "12345", object : IFTechEkycCallback<FTechEkycInfo> {
                override fun onSuccess(info: FTechEkycInfo?) {
                    tvState.text = info?.message
                }

                override fun onFail() {

                }

                override fun onCancel() {

                }
            })
        }
    }
}
