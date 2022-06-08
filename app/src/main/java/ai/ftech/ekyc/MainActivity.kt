package ai.ftech.ekyc

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.presentation.home.FEkycHomeActivity
import android.widget.TextView
import android.widget.Toast

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun onInitView() {
        super.onInitView()
//        findViewById<TextView>(R.id.tvTitle).setOnSafeClick {
//            navigateTo(FEkycHomeActivity::class.java)
//        }
        navigateTo(FEkycHomeActivity::class.java)
    }
}
