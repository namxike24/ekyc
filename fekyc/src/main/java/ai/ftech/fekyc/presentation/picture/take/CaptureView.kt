package ai.ftech.fekyc.presentation.picture.take

import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.common.widget.overlay.OverlayView
import ai.ftech.fekyc.utils.FileUtils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import java.io.File

class CaptureView constructor(
    ctx: Context,
    attrs: AttributeSet?
) : FrameLayout(ctx, attrs) {

    private var callback: ICallback? = null

    private var cvCamera: CameraView? = null
    private var ovFrameCrop: OverlayView? = null

    private var ivLeft: ImageView? = null
    private var leftIcon: Drawable? = null
    private var leftIconSize: Int = 0
    private var onLeftIconClick: (() -> Unit)? = null
    private var isShowLeftIcon: Boolean = true

    private var ivMid: ImageView? = null
    private var midIcon: Drawable? = null
    private var midIconSize: Int = 0
    private var onMidIconClick: (() -> Unit)? = null
    private var isShowMidIcon: Boolean = true

    private var ivRight: ImageView? = null
    private var rightIcon: Drawable? = null
    private var rightIconSize: Int = 0
    private var onRightIconClick: (() -> Unit)? = null
    private var isShowRightIcon: Boolean = true

    private var isBackFacing: Boolean = true
    private var isFlashOn: Boolean = false

    private var file: File? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.capture_layout, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        cvCamera = findViewById(R.id.cvCaptureCameraView)
        ivLeft = findViewById(R.id.ivCaptureLeft)
        ivMid = findViewById(R.id.ivCaptureMid)
        ivRight = findViewById(R.id.ivCaptureRight)
        ovFrameCrop = findViewById(R.id.ovCaptureFrameCrop)

        if (leftIcon != null) {
            ivLeft?.setImageDrawable(leftIcon)
        }
        if (midIcon != null) {
            ivMid?.setImageDrawable(midIcon)
        }
        if (rightIcon != null) {
            ivRight?.setImageDrawable(rightIcon)
        }

        if (leftIconSize != 0) {
            ivLeft?.layoutParams?.width = leftIconSize
            ivLeft?.layoutParams?.height = leftIconSize
        }
        if (midIconSize != 0) {
            ivMid?.layoutParams?.width = midIconSize
            ivMid?.layoutParams?.height = midIconSize
        }
        if (rightIconSize != 0) {
            ivRight?.layoutParams?.width = rightIconSize
            ivRight?.layoutParams?.height = rightIconSize
        }

        ivLeft?.visibility = if (isShowLeftIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }
        ivMid?.visibility = if (isShowMidIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }
        ivRight?.visibility = if (isShowRightIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }

        cvCamera?.facing = if (isBackFacing) {
            Facing.BACK
        } else {
            Facing.FRONT
        }

        cvCamera?.flash = if (isFlashOn) {
            Flash.TORCH
        } else {
            Flash.OFF
        }

        ivLeft?.setOnSafeClick {
            onLeftIconClick?.invoke()
        }
        ivMid?.setOnSafeClick {
            onMidIconClick?.invoke()
        }
        ivRight?.setOnSafeClick {
            onRightIconClick?.invoke()
        }

        cvCamera?.addCameraListener(object: CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                val path = FileUtils.getIdentityFrontPath()
                val file = File(path)
                if (file.exists()) {
                    FileUtils.deleteFile(path)
                }
                result.toFile(file) {
                    it?.let { file ->
                        this@CaptureView.file = file
                        ovFrameCrop?.attachFile(file.absolutePath)
                    }
                }
            }
        })

        ovFrameCrop?.apply {
            setCropType(OverlayView.CROP_TYPE.REACTANGLE)
            listener = object : OverlayView.ICallback {
                override fun onTakePicture(bitmap: Bitmap) {
                    val file = FileUtils.bitmapToFile(bitmap, file?.absolutePath.toString())
                    if (file != null) {
                        this@CaptureView.callback?.onCaptureSuccess(file)
                    }
                }

                override fun onError(exception: Exception) {
                    this@CaptureView.callback?.onCaptureFail(exception)
                }
            }
        }
    }

    fun setLifecycleOwner(owner: LifecycleOwner?) {
        cvCamera?.setLifecycleOwner(owner)
    }

    fun changeFlash() {
        setFlash(!isFlashOn)
    }

    fun changeFacing() {
        setFacing(!isBackFacing)
    }

    fun isFlashOn(): Boolean {
        return isFlashOn
    }

    fun isBackFacing(): Boolean {
        return isBackFacing
    }

    fun setFlash(isFlashOn: Boolean) {
        this.isFlashOn = isFlashOn
        cvCamera?.flash = if (isFlashOn) {
            Flash.TORCH
        } else {
            Flash.OFF
        }
    }

    fun setFacing(isBackFacing: Boolean) {
        this.isBackFacing = isBackFacing
        cvCamera?.facing = if (isBackFacing) {
            Facing.BACK
        } else {
            Facing.FRONT
        }
    }

    fun setOnLeftIconClick(onLeftIconClick: (() -> Unit)?) {
        this.onLeftIconClick = onLeftIconClick
    }

    fun setOnMidIconClick(onMidIconClick: (() -> Unit)?) {
        this.onMidIconClick = onMidIconClick
    }

    fun setOnRightIconClick(onRightIconClick: (() -> Unit)?) {
        this.onRightIconClick = onRightIconClick
    }

    fun setRightIcon(@DrawableRes iconId: Int) {
        rightIcon = ContextCompat.getDrawable(context, iconId)
        ivRight?.setImageDrawable(rightIcon)
    }

    fun setLeftIcon(@DrawableRes iconId: Int) {
        leftIcon = ContextCompat.getDrawable(context, iconId)
        ivLeft?.setImageDrawable(leftIcon)
    }

    fun setMidIcon(@DrawableRes iconId: Int) {
        midIcon = ContextCompat.getDrawable(context, iconId)
        ivMid?.setImageDrawable(midIcon)
    }

    fun setLeftIconSize(size: Int) {
        leftIconSize = size
        ivLeft?.layoutParams?.width = leftIconSize
        ivLeft?.layoutParams?.height = leftIconSize
    }

    fun setMidIconSize(size: Int) {
        midIconSize = size
        ivMid?.layoutParams?.width = midIconSize
        ivMid?.layoutParams?.height = midIconSize
    }

    fun setRightIconSize(size: Int) {
        rightIconSize = size
        ivRight?.layoutParams?.width = rightIconSize
        ivRight?.layoutParams?.height = rightIconSize
    }

    fun setLeftIconVisibility(isShow: Boolean) {
        isShowLeftIcon = isShow
        ivLeft?.visibility = if (isShowLeftIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun capture(callback: ICallback) {
        this.callback = callback
        cvCamera?.takePictureSnapshot()
    }

    fun setMidIconVisibility(isShow: Boolean) {
        isShowMidIcon = isShow
        ivMid?.visibility = if (isShowMidIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun setRightIconVisibility(isShow: Boolean) {
        isShowRightIcon = isShow
        ivRight?.visibility = if (isShowRightIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CaptureView, 0, 0)

        if (ta.hasValue(R.styleable.CaptureView_captureLeftIcon)) {
            leftIcon = ta.getDrawable(R.styleable.CaptureView_captureLeftIcon)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureMiddleIcon)) {
            midIcon = ta.getDrawable(R.styleable.CaptureView_captureMiddleIcon)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureRightIcon)) {
            rightIcon = ta.getDrawable(R.styleable.CaptureView_captureRightIcon)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureLeftIconSize)) {
            leftIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureLeftIconSize, 0)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureMiddleIconSize)) {
            midIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureMiddleIconSize, 0)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureRightIconSize)) {
            rightIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureRightIconSize, 0)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureLeftIconVisibility)) {
            isShowLeftIcon = ta.getBoolean(R.styleable.CaptureView_captureLeftIconVisibility, true)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureMiddleIconVisibility)) {
            isShowMidIcon = ta.getBoolean(R.styleable.CaptureView_captureMiddleIconVisibility, true)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureRightIconVisibility)) {
            isShowRightIcon = ta.getBoolean(R.styleable.CaptureView_captureRightIconVisibility, true)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureCameraFacing)) {
            isBackFacing = ta.getBoolean(R.styleable.CaptureView_captureCameraFacing, true)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFlash)) {
            isFlashOn = ta.getBoolean(R.styleable.CaptureView_captureFlash, false)
        }

        ta.recycle()
    }

    interface ICallback {
        fun onCaptureSuccess(file: File)
        fun onCaptureFail(exception: Exception)
    }
}
