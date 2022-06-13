package ai.ftech.ekyc

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.presentation.home.HomeActivity
import android.widget.TextView

class MainActivity : BaseActivity(R.layout.activity_main) {

    override fun onInitView() {
        super.onInitView()
        findViewById<TextView>(R.id.tvTitle).setOnSafeClick {
            navigateTo(HomeActivity::class.java)
        }
        navigateTo(HomeActivity::class.java)
    }
}
