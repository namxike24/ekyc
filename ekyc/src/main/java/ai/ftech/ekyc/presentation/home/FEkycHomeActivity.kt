package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FEkycHomeActivity : FEkycActivity(R.layout.fekyc_home_activity) {
    private lateinit var hvToolbar: ToolbarView
    private lateinit var rvListStep: RecyclerView
    private lateinit var llHomeSSN: LinearLayout
    private lateinit var llHomeDriverLicense: LinearLayout
    private lateinit var llHomePassport: LinearLayout
    private val adapter by lazy {
        StepIdentityAdapter()
    }

    override fun onInitView() {
        super.onInitView()
        hvToolbar = findViewById(R.id.hvHomeToolbar)
        rvListStep = findViewById(R.id.rvHomeListStep)


        rvListStep.layoutManager = LinearLayoutManager(this)
        rvListStep.adapter = adapter

        hvToolbar.setListener(object : ToolbarView.IHeaderViewListener {
            override fun onCloseClick() {
                finish()
            }

            override fun onLeftTextClick() {

            }
        })

        llHomeSSN.setOnSafeClick {

        }

        llHomeDriverLicense.setOnSafeClick {

        }

        llHomePassport.setOnSafeClick {

        }
    }
}
