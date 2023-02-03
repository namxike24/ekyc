package ai.ftech.fekyc.common.widget.leftcard

import ai.ftech.fekyc.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearancePathProvider

open class LeafCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var cornerTopLeft = 0f
    private var cornerTopRight = 0f
    private var cornerBottomLeft = 0f
    private var cornerBottomRight = 0f
    private val rectView = RectF(0f, 0f, 0f, 0f)
    private val path = Path()
    private val pathProvider = ShapeAppearancePathProvider()

    init {
        init(attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectView.right = w.toFloat()
        rectView.bottom = h.toFloat()
        pathProvider.calculatePath(shapeAppearanceModel, 1f, rectView, path)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }

    fun setCorner(corner: Float) {
        cornerTopLeft = corner
        cornerTopRight = corner
        cornerBottomLeft = corner
        cornerBottomRight = corner
        shapeAppearance()
        requestLayout()
    }

    fun setCorner(topLeft: Float = 0f,
                  topRight: Float = 0f,
                  bottomLeft: Float = 0f,
                  bottomRight: Float = 0f) {

        cornerTopLeft = topLeft
        cornerTopRight = topRight
        cornerBottomLeft = bottomLeft
        cornerBottomRight = bottomRight
        shapeAppearance()
        requestLayout()
    }

    private fun shapeAppearance() {
        shapeAppearanceModel = shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, cornerTopLeft)
            .setTopRightCorner(CornerFamily.ROUNDED, cornerTopRight)
            .setBottomLeftCorner(CornerFamily.ROUNDED, cornerBottomLeft)
            .setBottomRightCorner(CornerFamily.ROUNDED, cornerBottomRight)
            .build()
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.LeafCardView, 0, 0)
        cornerTopLeft = ta.getDimensionPixelSize(R.styleable.LeafCardView_lcv_corner_top_left, 0).toFloat()
        cornerTopRight = ta.getDimensionPixelSize(R.styleable.LeafCardView_lcv_corner_top_right, 0).toFloat()
        cornerBottomLeft = ta.getDimensionPixelSize(R.styleable.LeafCardView_lcv_corner_bottom_left, 0).toFloat()
        cornerBottomRight = ta.getDimensionPixelSize(R.styleable.LeafCardView_lcv_corner_bottom_right, 0).toFloat()
        shapeAppearance()
        ta.recycle()
    }
}
