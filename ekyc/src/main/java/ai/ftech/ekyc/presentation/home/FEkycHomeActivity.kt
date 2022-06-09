package ai.ftech.ekyc.presentation.home

import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.header.HeaderView

class FEkycHomeActivity : FEkycActivity(R.layout.fekyc_home_activity) {
    private lateinit var hvToolbar: HeaderView
    private val adapter = StepIdentityAdapter()

    override fun onInitView() {
        super.onInitView()
        hvToolbar = findViewById(R.id.hvHomeToolbar)

        hvToolbar.setListener(object : HeaderView.IHeaderViewListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onLeftTextClick() {

            }
        })
    }
}
