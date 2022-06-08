package ai.ftech.ekyc.common.widget.header

import ai.ftech.ekyc.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class HeaderView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(ctx, attrs) {

    private lateinit var ivClose : ImageView
    private lateinit var tvTitle : TextView
    private lateinit var tvLeftText : TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_header_layout, this, true)
    }


    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.InputText, 0, 0)

    }

}
