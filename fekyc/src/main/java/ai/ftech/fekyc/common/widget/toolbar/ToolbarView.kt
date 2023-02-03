package ai.ftech.fekyc.common.widget.toolbar

import ai.ftech.fekyc.base.extension.gone
import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.base.extension.show
import ai.ftech.fekyc.R
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

    private lateinit var ivLeftIcon: ImageView

    private lateinit var tvCenterTitle: TextView

    private lateinit var tvRightText: TextView
    private lateinit var ivRightIcon: ImageView

    private var listener: IListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_toolbar_layout, this, true)
        initView()
        init(attrs)
    }

    fun setTitle(char: CharSequence) {
        tvCenterTitle.text = char
    }

    fun clearTitle() {
        tvCenterTitle.text = ""
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
        ivLeftIcon = findViewById(R.id.ivToolbarLeftIcon)
        tvCenterTitle = findViewById(R.id.tvToolbarCenterTitle)
        tvRightText = findViewById(R.id.tvToolbarRightText)
        ivRightIcon = findViewById(R.id.ivToolbarRightIcon)

        ivLeftIcon.setOnSafeClick {
            listener?.onLeftIconClick()
        }

        tvRightText.setOnSafeClick {
            listener?.onRightTextClick()
        }

        ivRightIcon.setOnSafeClick {
            listener?.onRightIconClick()
        }
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.ToolbarView, 0, 0)

        val leftIcon = ta.getDrawable(R.styleable.ToolbarView_tbv_left_icon_src)
        ivLeftIcon.setImageDrawable(leftIcon)

        tvCenterTitle.text = ta.getText(R.styleable.ToolbarView_tbv_title_text)
        tvCenterTitle.textSize = ta.getDimension(R.styleable.ToolbarView_tbv_title_text_size, 12f)

        tvRightText.text = ta.getText(R.styleable.ToolbarView_tbv_right_text_content)
        tvRightText.textSize = ta.getDimension(R.styleable.ToolbarView_tbv_right_text_text_size, 12f)

        when (ta.getInt(R.styleable.ToolbarView_tbv_right_type, RIGHT_TYPE_NONE)) {
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
        fun onLeftIconClick() {}
        fun onRightTextClick() {}
        fun onRightIconClick() {}
    }
}
