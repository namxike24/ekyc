package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.FormInfo
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EkycInfoActivity : FEkycActivity(R.layout.fekyc_ekyc_info_activity) {
    private lateinit var constRoot: ConstraintLayout
    private lateinit var tbvHeader: ToolbarView
    private lateinit var tvTypePapres: TextView
    private lateinit var rvUserInfo: RecyclerView
    private lateinit var btnCompleted: Button
    private val viewModel by viewModels<EkycInfoViewModel>()

    private val adapter = FormInfoAdapter().apply {
        listener = object : FormInfoAdapter.IListener {
            override fun onClickItem(item: FormInfo) {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        addTouchRootListener(constRoot, object : ITouchOutsideViewListener {
            override fun onTouchOutside(view: View, event: MotionEvent) {
                hideKeyboard()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        removeTouchRootListener()
    }

    override fun onInitView() {
        super.onInitView()
        constRoot = findViewById(R.id.constEkycInfoRoot)
        tbvHeader = findViewById(R.id.tbvEkycInfoHeader)
        tvTypePapres = findViewById(R.id.tvEkycInfoEkycTypeContent)
        rvUserInfo = findViewById(R.id.rvEkycInfoList)
        btnCompleted = findViewById(R.id.btnEkycInfoCompleted)

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                val dialog = ConfirmDialog.Builder()
                    .setTitle(getAppString(R.string.fekyc_confirm_notification_title))
                    .setContent(getAppString(R.string.fekyc_confirm_notification_content))
                    .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
                    .setRightTitle(getAppString(R.string.fekyc_confirm_stay))
                    .build()
                dialog.listener = object : ConfirmDialog.IListener {
                    override fun onLeftClick() {
                        finish()
                    }

                    override fun onRightClick() {
                        dialog.dismissDialog()
                    }
                }
                dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
            }
        })

        rvUserInfo.layoutManager = LinearLayoutManager(this)
        rvUserInfo.adapter = adapter

        viewModel.getEkycInfo()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        observer(viewModel.ekycInfo) {
            tvTypePapres.text = String.format("${it?.identityType}: ${it?.identityName}")
            adapter.resetData(it?.formList ?: emptyList())
        }
    }

}
