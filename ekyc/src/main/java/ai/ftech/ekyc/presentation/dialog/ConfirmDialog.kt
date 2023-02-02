package ai.ftech.ekyc.presentation.dialog

import ai.ftech.ekyc.base.common.BaseDialog
import ai.ftech.ekyc.base.common.DialogScreen
import ai.ftech.ekyc.base.extension.gone
import ai.ftech.ekyc.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import android.widget.Button
import android.widget.TextView

class ConfirmDialog : BaseDialog(R.layout.fekyc_confirm_dialog) {
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnLeft: Button
    private lateinit var btnRight: Button
    var builder: Builder? = null
    var listener: IListener? = null

    override fun onInitView() {
        tvTitle = viewRoot.findViewById(R.id.tvConfirmDlgTitle)
        tvContent = viewRoot.findViewById(R.id.tvConfirmDlgContent)
        btnLeft = viewRoot.findViewById(R.id.btnConfirmDlgLeft)
        btnRight = viewRoot.findViewById(R.id.btnConfirmDlgRight)


        tvTitle.text = builder?.title
        tvContent.text = builder?.content

        btnLeft.setOnSafeClick {
            listener?.onLeftClick()
        }

        btnRight.setOnSafeClick {
            listener?.onRightClick()
        }

        btnRight.text = builder?.rightTitle
        btnLeft.text = builder?.leftTitle

        if (builder?.leftTitle.isNullOrEmpty()) {
            btnLeft.gone()
        }

        if (builder?.rightTitle.isNullOrEmpty()) {
            btnRight.gone()
        }
    }

    override fun getBackgroundId() = R.id.frameConfirmDlgRoot

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
        var leftTitle: String? = null
            private set
        var rightTitle: String? = null
            private set

        fun build(): ConfirmDialog {
            val dialog = ConfirmDialog()
            dialog.builder = this
            return dialog
        }

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setContent(content: String) = apply {
            this.content = content
        }

        fun setLeftTitle(leftTitle: String) = apply {
            this.leftTitle = leftTitle
        }

        fun setRightTitle(rightTitle: String) = apply {
            this.rightTitle = rightTitle
        }
    }

    interface IListener {
        fun onLeftClick() {}
        fun onRightClick() {}
    }
}
