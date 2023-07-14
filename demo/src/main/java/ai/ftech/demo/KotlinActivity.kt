package ai.ftech.demo

import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.publish.FTechEkycInfo
import ai.ftech.fekyc.publish.FTechEkycManager
import ai.ftech.fekyc.publish.IFTechEkycCallback
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class KotlinActivity : AppCompatActivity() {
    private lateinit var tvState: TextView
    private lateinit var btnEkyc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity)

        tvState = findViewById(R.id.tvDemoState)

        tvState.setOnClickListener {
            tvState.text = ""
        }

        btnEkyc.setOnClickListener {
            FTechEkycManager.startEkyc("licenceftechekyc", "ftechekycapp", "${Random.nextInt(1, 100000)}", object : IFTechEkycCallback<FTechEkycInfo> {
                override fun onSuccess(info: FTechEkycInfo?) {
                    tvState.text = info?.message
                }

                override fun onFail(error: APIException) {

                }

                override fun onCancel() {

                }
            })
        }
    }
}
