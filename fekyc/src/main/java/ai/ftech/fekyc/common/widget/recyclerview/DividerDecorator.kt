package ai.ftech.fekyc.common.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

open class DividerDecorator : RecyclerView.ItemDecoration {
    var drawable: Drawable? = null
        private set
    private var left = 0
    private var top = 0
    private var right = 0
    private var bottom = 0
    private val dividerOutRect = Rect()
    private var isShowLast = false
    private var isDrawOver = false

    constructor(context: Context, resId: Int, isShowLast: Boolean = false, isDrawOver: Boolean = false) {
        this.drawable = ContextCompat.getDrawable(context, resId)
        this.isShowLast = isShowLast
        this.isDrawOver = isDrawOver
    }

    constructor(drawable: Drawable, isShowLast: Boolean = false, isDrawOver: Boolean = false) {
        this.drawable = drawable
        this.isShowLast = isShowLast
        this.isDrawOver = isDrawOver
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            return
        }
        if (isDrawOver) {
            outRect.bottom = outRect.top
        } else {
            outRect.bottom = drawable!!.intrinsicHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (!isDrawOver) {
            drawDivider(c, parent, state)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (isDrawOver) {
            drawDivider(c, parent, state)
        }
    }

    /**
     * Determine bound of divider. This bound decide the position of divider
     *
     * @param rect   bound of divider
     * @param view
     * @param parent
     * @param state
     */
    open fun getDividerOffset(
        rect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val dividerLeft = parent.paddingLeft + left
        val dividerTop = view.bottom + params.bottomMargin + top
        val dividerRight = parent.width - parent.paddingRight - right
        val dividerBottom = dividerTop + drawable!!.intrinsicHeight - bottom
        rect[dividerLeft, dividerTop, dividerRight] = dividerBottom
    }

    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    private fun drawDivider(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft + left
        val dividerRight = parent.width - parent.paddingRight - right
        val count = parent.childCount
        for (i in 0 until count) {
            if (i == count - 1 && !isShowLast) {
                return
            }
            val child = parent.getChildAt(i)
            getDividerOffset(dividerOutRect, child, parent, state)
            if (dividerOutRect.height() == 0) {
                continue
            } else {
                drawable!!.setBounds(
                    dividerOutRect.left,
                    dividerOutRect.top,
                    dividerOutRect.right,
                    dividerOutRect.bottom
                )
                drawable!!.draw(c)
            }
            /*RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int dividerTop = child.getBottom() + params.bottomMargin + mTop;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight() - mBottom;
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);*/
        }
    }
}
