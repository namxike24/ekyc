package ai.ftech.fekyc.presentation.dialog

import ai.ftech.fekyc.base.common.BaseDialog
import ai.ftech.fekyc.base.common.DialogScreen
import ai.ftech.fekyc.R
import android.widget.TextView

class LoadingDialog : BaseDialog(R.layout.fekyc_loading_dialog) {
    private lateinit var tvMessage: TextView
    var message: String? = null

    override fun getBackgroundId() = R.id.constLoadingDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.NORMAL
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = false
    }

    override fun onInitView() {
        tvMessage = viewRoot.findViewById(R.id.tvLoadingDlgMessage)

        message?.let {
            tvMessage.text = it
        }
    }
}
