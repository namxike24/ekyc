package ai.ftech.ekyc.common.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomSpaceDecoration(private val space: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        parent.adapter?.itemCount?.let { itemCount ->
            if (parent.getChildAdapterPosition(view) == (itemCount - 1)) {
                outRect.bottom += space;
            }
        }
    }
}
