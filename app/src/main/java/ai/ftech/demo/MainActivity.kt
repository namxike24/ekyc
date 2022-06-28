package ai.ftech.demo

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.FTechEkycManager
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.event.EkycEvent
import android.content.Intent
import android.widget.TextView

class MainActivity : BaseActivity(R.layout) {

    companion object {
        private const val EKYC_REQUEST_CODE = 123
    }

    override fun onInitView() {
        super.onInitView()
        findViewById<TextView>(R.id.tvTitle).setOnSafeClick {
//            navigateTo(HomeActivity::class.java)

            FTechEkycManager.openEkyc(this, EKYC_REQUEST_CODE)
        }
//        navigateTo(HomeActivity::class.java)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val event = data?.getParcelableExtra<EkycEvent>(FTechEkycManager.EKYC_RESULT_DATA)

    }
}
