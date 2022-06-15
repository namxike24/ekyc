package ai.ftech.ekyc.common.widget.overlay

import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable

fun Drawable.drawAt(rect: RectF, canvas: Canvas) {
    setBounds(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
    draw(canvas)
}
