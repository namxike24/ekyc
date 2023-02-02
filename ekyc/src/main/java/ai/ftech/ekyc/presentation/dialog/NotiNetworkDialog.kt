package ai.ftech.ekyc.presentation.dialog

import ai.ftech.ekyc.base.common.BaseDialog
import ai.ftech.ekyc.base.common.DialogScreen
import ai.ftech.ekyc.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import android.widget.Button
import android.widget.TextView

class NotiNetworkDialog : BaseDialog(R.layout.fekyc_noti_network_dialog) {
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnClose: Button
    var builder: Builder? = null

    override fun onInitView() {
        tvTitle = viewRoot.findViewById(R.id.tvNotiNetworkDlgTitle)
        tvContent = viewRoot.findViewById(R.id.tvNotiNetworkDlgContent)
        btnClose = viewRoot.findViewById(R.id.btnNotiNetworkDlgClose)

        tvTitle.text = builder?.title
        tvContent.text = builder?.content

        btnClose.setOnSafeClick {
            dismissDialog()
        }
    }

    override fun getBackgroundId() = R.id.frameNotiNetworkDlgRoot

    override fun screen(): DialogScreen {
        return DialogScreen().apply {
            mode = DialogScreen.DIALOG_MODE.NORMAL
            isFullHeight = true
            isFullWidth = true
        }
    }

    class Builder {
        var title: String? = null
            private set
        var content: String? = null
            private set

        fun build(): NotiNetworkDialog {
            val dialog = NotiNetworkDialog()
            dialog.builder = this
            return dialog
        }

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setContent(content: String) = apply {
            this.content = content
        }
    }
}
