package ai.ftech.fekyc.common.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

open class SpaceItemDecoration(val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            setSpaceForGridLayout(
                layoutManager.orientation,
                layoutManager.spanCount,
                outRect,
                view,
                parent
            )
        } else if (layoutManager is LinearLayoutManager) {
            setSpaceForLinearLayout(layoutManager.orientation, outRect, view, parent)
        }
    }

    private fun setSpaceForLinearLayout(
        orientation: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.top = space
                    outRect.bottom = space / 2
                }
                parent.adapter!!.itemCount - 1 -> {
                    outRect.bottom = space
                    outRect.top = space / 2
                }
                else -> {
                    outRect.top = space / 2
                    outRect.bottom = space / 2
                }
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    outRect.left = space
                    outRect.right = space / 2
                }
                parent.adapter!!.itemCount - 1 -> {
                    outRect.right = space
                    outRect.left = space / 2
                }
                else -> {
                    outRect.left = space / 2
                    outRect.right = space / 2
                }
            }
        }
    }

    private fun setSpaceForGridLayout(
        orientation: Int,
        spanCount: Int,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.top = if (isFirstRowInVerticalGrid(position, spanCount)) {
                0
            } else {
                space / 2
            }
            outRect.bottom = if (isLastRowInVerticalGrid(position, spanCount, parent.adapter!!.itemCount)) {
                0
            } else {
                space / 2
            }
            outRect.left = if (isFirstColumnInVerticalGrid(position, spanCount)) {
                space
            } else {
                space / 2
            }
            outRect.right = if (isLastColumnInVerticalGrid(position, spanCount)) {
                space
            } else {
                space / 2
            }
        } else if (orientation == LinearLayoutManager.HORIZONTAL) {
            outRect.left = if (isFirstColumnInHorizontalGrid(position, spanCount)) {
                space
            } else {
                space / 2
            }
            outRect.right = if (isLastColumnInHorizontalGrid(position, spanCount, parent.adapter!!.itemCount)) {
                space
            } else {
                space / 2
            }
            outRect.top = if (isFirstRowInHorizontalGrid(position, spanCount)) {
                0
            } else {
                space / 2
            }
            outRect.bottom = if (isLastRowInHorizontalGrid(position, spanCount)) {
                0
            } else {
                space / 2
            }
        }
    }

    private fun isFirstRowInVerticalGrid(position: Int, spanCount: Int): Boolean {
        return position < spanCount
    }

    private fun isLastRowInVerticalGrid(position: Int, spanCount: Int, total: Int): Boolean {
        val countItemInLastRow = if (total % spanCount == 0) {
            spanCount
        } else {
            total % spanCount
        }
        return position in ((total - countItemInLastRow) until total)
    }

    private fun isFirstColumnInVerticalGrid(position: Int, spanCount: Int): Boolean {
        return position % spanCount == 0
    }

    private fun isLastColumnInVerticalGrid(position: Int, spanCount: Int): Boolean {
        return position % spanCount == (spanCount - 1)
    }

    private fun isFirstRowInHorizontalGrid(position: Int, spanCount: Int): Boolean =
        isFirstColumnInVerticalGrid(position, spanCount)

    private fun isLastRowInHorizontalGrid(position: Int, spanCount: Int): Boolean =
        isLastColumnInVerticalGrid(position, spanCount)

    private fun isFirstColumnInHorizontalGrid(position: Int, spanCount: Int): Boolean =
        isFirstRowInVerticalGrid(position, spanCount)

    private fun isLastColumnInHorizontalGrid(position: Int, spanCount: Int, total: Int): Boolean =
        isLastRowInVerticalGrid(position, spanCount, total)
}
