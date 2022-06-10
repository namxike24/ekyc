package ai.ftech.ekyc.common.widget.toolbar

import ai.ftech.dev.base.extension.gone
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.dev.base.extension.show
import ai.ftech.ekyc.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

class ToolbarView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(ctx, attrs) {

    companion object {
        private const val RIGHT_TYPE_NONE = 0
        private const val RIGHT_TYPE_TEXT = 1
        private const val RIGHT_TYPE_ICON = 2
    }

    private lateinit var ivClose: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvRightText: TextView
    private lateinit var ivRightIcon: ImageView
    private var listener: IListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_toolbar_layout, this, true)
        initView()
        init(attrs)
    }

    fun setTitle(char: CharSequence) {
        tvTitle.text = char
    }

    fun clearTitle() {
        tvTitle.text = ""
    }

    fun setLeftText(char: CharSequence) {
        tvRightText.text = char
    }

    fun clearLeftText() {
        tvRightText.text = ""
    }

    fun setListener(listener: IListener) {
        this.listener = listener
    }

    private fun initView() {
        ivClose = findViewById(R.id.ivToolbarClose)
        tvTitle = findViewById(R.id.tvToolbarTitle)
        tvRightText = findViewById(R.id.tvToolbarRightText)
        ivRightIcon = findViewById(R.id.ivToolbarRightIcon)

        ivClose.setOnSafeClick {
            listener?.onCloseClick()
        }

        tvRightText.setOnSafeClick {
            listener?.onLeftTextClick()
        }

        ivRightIcon.setOnSafeClick {
            listener?.onLeftIconClick()
        }
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.ToolbarView, 0, 0)

        tvTitle.text = ta.getText(R.styleable.ToolbarView_tbv_title_text)
        tvTitle.textSize = ta.getDimension(R.styleable.ToolbarView_tbv_title_text_size, 12f)

        tvRightText.text = ta.getText(R.styleable.ToolbarView_tbv_right_text_content)
        tvRightText.textSize = ta.getDimension(R.styleable.ToolbarView_tbv_right_text_text_size, 12f)

        val rightType = ta.getInt(R.styleable.ToolbarView_tbv_right_type, RIGHT_TYPE_NONE)
        when (rightType) {
            RIGHT_TYPE_NONE -> {
                tvRightText.gone()
                ivRightIcon.gone()
            }
            RIGHT_TYPE_TEXT -> {
                tvRightText.show()
                ivRightIcon.gone()
            }
            RIGHT_TYPE_ICON -> {
                tvRightText.gone()
                ivRightIcon.show()
            }
        }

        ta.recycle()
    }

    interface IListener {
        fun onCloseClick() {}
        fun onLeftTextClick() {}
        fun onLeftIconClick() {}
    }
}
