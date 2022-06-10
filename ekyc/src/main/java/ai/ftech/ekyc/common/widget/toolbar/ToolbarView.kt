package ai.ftech.ekyc.common.widget.toolbar

import ai.ftech.dev.base.extension.setOnSafeClick
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

    private lateinit var ivClose: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvLeftText: TextView
    private var listener: IHeaderViewListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_toolbar_layout, this, true)
        initView()
        init(attrs)
    }

    fun setTitle(char: String) {
        tvTitle.text = char
    }

    fun clearTitle() {
        tvTitle.text = ""
    }

    fun setLeftText(char: String) {
        tvLeftText.text = char
    }

    fun clearLeftText() {
        tvLeftText.text = ""
    }

    fun setListener(listener: IHeaderViewListener) {
        this.listener = listener
    }

    private fun initView() {
        ivClose = findViewById(R.id.ivToolbarClose)
        tvTitle = findViewById(R.id.tvToolbarTitle)
        tvLeftText = findViewById(R.id.tvToolbarLeftText)

        ivClose.setOnSafeClick {
            listener?.onCloseClick()
        }

        tvLeftText.setOnSafeClick {
            listener?.onLeftTextClick()
        }
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.HeaderView, 0, 0)

        tvTitle.text = ta.getText(R.styleable.HeaderView_hv_title_text)
        tvTitle.textSize = ta.getDimension(R.styleable.HeaderView_hv_title_text_size, 12f)

        tvLeftText.text = ta.getText(R.styleable.HeaderView_hv_lefttext_text)
        tvLeftText.textSize = ta.getDimension(R.styleable.HeaderView_hv_lefttext_text_size, 12f)

        ta.recycle()
    }

    interface IHeaderViewListener {
        fun onCloseClick() {}
        fun onLeftTextClick() {}
    }
}
