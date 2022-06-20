package ai.ftech.ekyc.common.widget.overlay

import ai.ftech.dev.base.extension.getAppColor
import ai.ftech.dev.base.extension.getAppDrawable
import ai.ftech.ekyc.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.io.File

class OverlayView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : View(ctx, attrs) {

    companion object {
        private val TAG = OverlayView::class.java.simpleName
        private const val THREAD_NAME_BY_CROP_BITMAP = "THREAD_NAME_BY_CROP_BITMAP"
        private const val THREAD_NAME_BY_RESIZE_BITMAP = "THREAD_NAME_BY_RESIZE_BITMAP"
        private const val IMAGE_CROP_MAX_SIZE = 960f
    }

    var listener: ICallback? = null
    private var rectBackground = RectF()
    private var paintBackground = Paint(Paint.ANTI_ALIAS_FLAG)

    private var rectFrame = RectF()
    private var paintFrame = Paint(Paint.ANTI_ALIAS_FLAG)
    private var drawableFrame: Drawable? = null

    private var bitmapFull: Bitmap? = null
    var imageCropMaxSize = IMAGE_CROP_MAX_SIZE
    private var executor: HandleViewPool = HandleViewPool()


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

//        canvas.drawRoundRect(rectFrame, getDrawableWidth() / 2f, getDrawableHeight() / 2f, paintFrame)
        canvas.drawRect(rectFrame, paintFrame)
//        drawableFrame?.drawAt(rectFrame, canvas)


        val w = ((rectBackground.width() - (144 * 2)) * 0.8).toInt()
        val h = ((rectBackground.height() - (804 * 2)) * 0.8).toInt()

        canvas.drawCircle(h.toFloat(), w.toFloat(), 5f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
        })
    }

    fun attachFile(path: String) {
        bitmapFull = BitmapFactory.decodeFile(path)
        cropBitmap()
    }

    private fun cropBitmap() {
        val rotationMatrix = Matrix()
        bitmapFull?.let {
            //xử lý ảnh bị xoay bởi camera Samsung
            if (it.width > it.height) {
//                rotationMatrix.postRotate(90f)
//
//                val ratio = it.height.toDouble() / width
//
//                val x = (rectFrame.left * ratio).toInt()
//                val y = (rectFrame.top * ratio).toInt()
//
//                val w = rectFrame.width().toInt()
//                val h = rectFrame.height().toInt()
//
//                Log.d(TAG, "cropBitmap:  ratio: $ratio x: $x  y: $y   w: $w   h: $h     bitmap[${it.width}   ${it.height}]    rectBackground[${rectBackground.width()}   ${rectBackground.height()}]")
//
//                val bitmap = Bitmap.createBitmap(it, x, y, w - x, h - x, rotationMatrix, false)
//
//                listener?.onTakePicture(bitmap, path)

                // TODO: xử lý case này sau
                listener?.onError(Exception("xoay thẳng điện thoại!"))

            } else {
                rotationMatrix.postRotate(0f)

                val ratio = it.width.toDouble() / width

                val x = (rectFrame.left * ratio).toInt()
                val y = (rectFrame.top * ratio).toInt()

                val w = rectFrame.width().toInt()
                val h = rectFrame.height().toInt()

                Log.d(TAG, "cropBitmap:  ratio: $ratio x: $x  y: $y   w: $w   h: $h     bitmap[${it.width}   ${it.height}]    rectBackground[${rectBackground.width()}   ${rectBackground.height()}]")

                val runnable = HandleViewTask(
                    name = THREAD_NAME_BY_CROP_BITMAP,
                    task = {
                        Bitmap.createBitmap(it, x, y, w - x, h - x, rotationMatrix, false)
                    },
                    callback = object : HandleViewTask.ICallback {
                        override fun onFinish(bitmap: Bitmap) {
                            resizeBitmap(bitmap)
                        }

                        override fun onError(exception: Exception) {
                            listener?.onError(exception)
                        }
                    })
                executor.execute(runnable)
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap) {
        val runnable = HandleViewTask(
            name = THREAD_NAME_BY_RESIZE_BITMAP,
            task = {
                val matrix = Matrix()
                val oldRect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
                val newRect = RectF(0f, 0f, imageCropMaxSize, imageCropMaxSize)
                matrix.setRectToRect(oldRect, newRect, Matrix.ScaleToFit.CENTER)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            },
            callback = object : HandleViewTask.ICallback {
                override fun onFinish(bitmap: Bitmap) {
                    listener?.onTakePicture(bitmap)
                }

                override fun onError(exception: Exception) {
                    listener?.onError(exception)
                }
            })
        executor.execute(runnable)
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

    interface ICallback {
        fun onTakePicture(bitmap: Bitmap)
        fun onError(exception: Exception)
    }
}
