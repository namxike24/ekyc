package ai.ftech.ekyc.common.widget.overlay

import ai.ftech.dev.base.extension.getAppColor
import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.ekyc.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout

class OverlayView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : FrameLayout(ctx, attrs) {

    companion object {
        private const val RADIUS_DEFAULT = 450f
    }

    private var bitmap: Bitmap? = null
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var drawableFrameTakePicture: Drawable? = null

    private var paintStroke = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        init(attrs)
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (bitmap == null) {
            createFrame()
        }
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    private fun createFrame() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val osCanvas = Canvas(bitmap!!)
        val outerRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        paint.color = getAppColor(R.color.fekyc_color_black_80)
        osCanvas.drawRect(outerRect, paint)

        paint.color = Color.TRANSPARENT
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        val strokeCanvas = Canvas()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.strokeWidth = 2f


        val centerX = width / 2f
        val centerY = height / 2f
        val radius = RADIUS_DEFAULT
        osCanvas.drawCircle(centerX, centerY, radius, paint)


        strokeCanvas.drawCircle(centerX, centerY, radius, paintStroke)
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.OverlayView, 0, 0)
        drawableFrameTakePicture = ta.getDrawable(R.styleable.OverlayView_ov_frame_take_picture)
            ?: getAppDrawable(R.drawable.bg_circle_stroke_blue)

        ta.recycle()
    }
}
