package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.UserInfo
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EkycInfoActivity : FEkycActivity(R.layout.fekyc_user_info_activity) {
    private lateinit var tbvHeader: ToolbarView
    private lateinit var tvTypePapres: TextView
    private lateinit var rvUserInfo: RecyclerView
    private lateinit var btnCompleted: Button

    private val adapter = UserInfoAdapter().apply {
        listener = object : UserInfoAdapter.IListener {
            override fun onClickItem(item: UserInfo) {

            }
        }
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvUserInfoHeader)
        tvTypePapres = findViewById(R.id.tvUserInfoEkycTypeContent)
        rvUserInfo = findViewById(R.id.rvUserInfoList)
        btnCompleted = findViewById(R.id.btnUserInfoCompleted)

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
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

    }

}
