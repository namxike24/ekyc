package ai.ftech.ekyc.common.widget.overlay

import ai.ftech.dev.base.extension.getAppColor
import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.ekyc.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : View(ctx, attrs) {

    companion object {
    }

    private var rectBackground = RectF()
    private var paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)

    private var rectFrame = RectF()
    private var paintFrame = Paint(Paint.ANTI_ALIAS_FLAG)
    private var drawableFrame: Drawable? = null

    init {

        init(attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initBackgroundView()
        initFrameView()

        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawRect(rectBackground, paintBackground)

        canvas.drawRoundRect(rectFrame, getDrawableWidth() / 2f, getDrawableHeight() / 2f, paintFrame)
        drawableFrame?.drawAt(rectFrame, canvas)
    }

    private fun getDrawableWidth(): Float {
        return drawableFrame?.intrinsicWidth?.toFloat() ?: 0f
    }

    private fun getDrawableHeight(): Float {
        return drawableFrame?.intrinsicHeight?.toFloat() ?: 0f
    }

    private fun initBackgroundView() {
        paintBackground.apply {
            color = getAppColor(R.color.fekyc_color_black_80)
        }
        rectBackground.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    private fun initFrameView() {
        paintFrame.apply {
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        }

        val centerX = rectBackground.centerX()
        val centerY = rectBackground.centerY()
        val halfOffsetWidth = getDrawableWidth() / 2f
        val halfOffsetHeight = getDrawableHeight() / 2f

        val left = centerX - halfOffsetWidth
        val top = centerY - halfOffsetHeight
        val right = centerX + halfOffsetWidth
        val bottom = centerY + halfOffsetHeight

        rectFrame.set(left, top, right, bottom)
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.OverlayView, 0, 0)
        drawableFrame = ta.getDrawable(R.styleable.OverlayView_ov_frame_take_picture)
            ?: getAppDrawable(R.drawable.bg_circle_stroke_blue)

        ta.recycle()
    }
}
