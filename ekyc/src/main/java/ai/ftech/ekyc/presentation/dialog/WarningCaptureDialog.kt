package ai.ftech.ekyc.presentation.dialog

import ai.ftech.dev.base.common.BaseDialog
import ai.ftech.dev.base.common.DialogScreen
import ai.ftech.ekyc.R

class WarningCaptureDialog : BaseDialog(R.layout.fekyc_warning_capture_dialog) {

    override fun onInitView() {

    }

    override fun getBackgroundId() = R.id.frameWarningCaptureDlgRoot

    override fun screen(): DialogScreen {
        return DialogScreen().apply {
            mode = DialogScreen.DIALOG_MODE.BOTTOM
            isFullWidth = true
        }
    }
}
