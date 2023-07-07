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

    private var ivFlash: ImageView? = null
    private var flashIconOn: Drawable? = null
    private var flashIconOff: Drawable? = null
    private var flashIconSize: Int = 0
    private var onFlashIconClick: (() -> Unit)? = null
    private var isShowFlashIcon: Boolean = true

    private var ivTakePicture: ImageView? = null
    private var takePictureIcon: Drawable? = null
    private var takePictureIconSize: Int = 0
    private var onTakePictureIconClick: (() -> Unit)? = null
    private var isShowTakePictureIcon: Boolean = true

    private var ivFacing: ImageView? = null
    private var facingIcon: Drawable? = null
    private var facingIconSize: Int = 0
    private var onFacingIconClick: (() -> Unit)? = null
    private var isShowFacingIcon: Boolean = true

    private var facing: FACING = FACING.BACK
    private var flash: FLASH = FLASH.OFF
    private var shape: SHAPE = SHAPE.RECTANGLE

    private var file: File? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.capture_layout, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        cvCamera = findViewById(R.id.cvCaptureCameraView)
        ivFlash = findViewById(R.id.ivCaptureLeft)
        ivTakePicture = findViewById(R.id.ivCaptureMid)
        ivFacing = findViewById(R.id.ivCaptureRight)
        ovFrameCrop = findViewById(R.id.ovCaptureFrameCrop)

        if (flashIconOn == null) {
            flashIconOn = ContextCompat.getDrawable(context, R.drawable.fekyc_ic_flash_on)
        }
        if (flashIconOff == null) {
            flashIconOff = ContextCompat.getDrawable(context, R.drawable.fekyc_ic_flash_off)
        }
        if (flash == FLASH.OFF) {
            ivFlash?.setImageDrawable(flashIconOn)
        } else {
            ivFlash?.setImageDrawable(flashIconOff)
        }
        if (takePictureIcon != null) {
            ivTakePicture?.setImageDrawable(takePictureIcon)
        }
        if (facingIcon != null) {
            ivFacing?.setImageDrawable(facingIcon)
        }

        if (flashIconSize != 0) {
            ivFlash?.layoutParams?.width = flashIconSize
            ivFlash?.layoutParams?.height = flashIconSize
        }
        if (takePictureIconSize != 0) {
            ivTakePicture?.layoutParams?.width = takePictureIconSize
            ivTakePicture?.layoutParams?.height = takePictureIconSize
        }
        if (facingIconSize != 0) {
            ivFacing?.layoutParams?.width = facingIconSize
            ivFacing?.layoutParams?.height = facingIconSize
        }

        ivFlash?.visibility = if (isShowFlashIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }
        ivTakePicture?.visibility = if (isShowTakePictureIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }
        ivFacing?.visibility = if (isShowFacingIcon) {
            VISIBLE
        } else {
            INVISIBLE
        }

        cvCamera?.facing = if (facing == FACING.BACK) {
            Facing.BACK
        } else {
            Facing.FRONT
        }

        cvCamera?.flash = if (flash == FLASH.ON) {
            Flash.TORCH
        } else {
            Flash.OFF
        }

        ivFlash?.setOnSafeClick {
            changeFlash()
            onFlashIconClick?.invoke()
        }
        ivTakePicture?.setOnSafeClick {
            takePicture()
            onTakePictureIconClick?.invoke()
        }
        ivFacing?.setOnSafeClick {
            changeFacing()
            onFacingIconClick?.invoke()
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
            setCropType(if (shape == SHAPE.RECTANGLE) {
                OverlayView.CROP_TYPE.REACTANGLE
            } else {
                OverlayView.CROP_TYPE.CIRCLE
            })
            listener = object : OverlayView.ICallback {
                override fun onTakePicture(bitmap: Bitmap) {
                    val file = FileUtils.bitmapToFile(bitmap, file?.absolutePath.toString())
                    if (file != null) {
                        this@CaptureView.callback?.onTakePictureSuccess(file)
                    }
                }

                override fun onError(exception: Exception) {
                    this@CaptureView.callback?.onTakePictureFail(exception)
                }
            }
        }
    }

    fun setLifecycleOwner(owner: LifecycleOwner?) {
        cvCamera?.setLifecycleOwner(owner)
    }

    fun changeFlash() {
        if (flash == FLASH.ON) {
            setFlash(FLASH.OFF)
        } else {
            setFlash(FLASH.ON)
        }
    }

    fun changeFacing() {
        if (facing == FACING.BACK) {
            setFacing(FACING.FRONT)
        } else {
            setFacing(FACING.BACK)
        }
    }

    fun getFlash(): FLASH {
        return flash
    }

    fun getFacing(): FACING {
        return facing
    }

    fun setFlash(flash: FLASH) {
        this.flash = flash
        if (flash == FLASH.ON) {
            cvCamera?.flash = Flash.TORCH
            ivFlash?.setImageDrawable(flashIconOff)
        } else {
            cvCamera?.flash = Flash.OFF
            ivFlash?.setImageDrawable(flashIconOn)
        }
    }

    fun setFacing(facing: FACING) {
        this.facing = facing
        cvCamera?.facing = if (facing == FACING.BACK) {
            Facing.BACK
        } else {
            Facing.FRONT
        }
    }

    fun setShape(shape: SHAPE) {
        this.shape = shape
        ovFrameCrop?.setCropType(if (shape == SHAPE.RECTANGLE) {
            OverlayView.CROP_TYPE.REACTANGLE
        } else {
            OverlayView.CROP_TYPE.CIRCLE
        })
    }

    fun setOnFlashIconClick(onFlashIconClick: (() -> Unit)?) {
        this.onFlashIconClick = onFlashIconClick
    }

    fun setOnTakePictureIconClick(onTakePictureIconClick: (() -> Unit)?) {
        this.onTakePictureIconClick = onTakePictureIconClick
    }

    fun setOnTakePictureCallback(callback: ICallback?) {
        this.callback = callback
    }

    fun setOnFacingIconClick(onFacingIconClick: (() -> Unit)?) {
        this.onFacingIconClick = onFacingIconClick
    }

    fun setFacingIcon(@DrawableRes iconId: Int) {
        facingIcon = ContextCompat.getDrawable(context, iconId)
        ivFacing?.setImageDrawable(facingIcon)
    }

    fun setFlashOnIcon(@DrawableRes iconId: Int) {
        flashIconOn = ContextCompat.getDrawable(context, iconId)
        if (flash == FLASH.OFF) {
            ivFlash?.setImageDrawable(flashIconOn)
        }
    }

    fun setFlashOffIcon(@DrawableRes iconId: Int) {
        flashIconOff = ContextCompat.getDrawable(context, iconId)
        if (flash == FLASH.ON) {
            ivFlash?.setImageDrawable(flashIconOff)
        }
    }

    fun setTakePictureIcon(@DrawableRes iconId: Int) {
        takePictureIcon = ContextCompat.getDrawable(context, iconId)
        ivTakePicture?.setImageDrawable(takePictureIcon)
    }

    fun setFlashIconSize(size: Int) {
        flashIconSize = size
        ivFlash?.layoutParams?.width = flashIconSize
        ivFlash?.layoutParams?.height = flashIconSize
    }

    fun setTakePictureIconSize(size: Int) {
        takePictureIconSize = size
        ivTakePicture?.layoutParams?.width = takePictureIconSize
        ivTakePicture?.layoutParams?.height = takePictureIconSize
    }

    fun setFacingIconSize(size: Int) {
        facingIconSize = size
        ivFacing?.layoutParams?.width = facingIconSize
        ivFacing?.layoutParams?.height = facingIconSize
    }

    fun setFlashIconVisibility(isShow: Boolean) {
        isShowFlashIcon = isShow
        ivFlash?.visibility = if (isShowFlashIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun takePicture() {
        cvCamera?.takePictureSnapshot()
    }

    fun setTakePictureIconVisibility(isShow: Boolean) {
        isShowTakePictureIcon = isShow
        ivTakePicture?.visibility = if (isShowTakePictureIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    fun setFacingIconVisibility(isShow: Boolean) {
        isShowFacingIcon = isShow
        ivFacing?.visibility = if (isShowFacingIcon) {
            VISIBLE
        } else {
            GONE
        }
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CaptureView, 0, 0)

        if (ta.hasValue(R.styleable.CaptureView_captureFlashOnIcon)) {
            flashIconOn = ta.getDrawable(R.styleable.CaptureView_captureFlashOnIcon)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFlashOffIcon)) {
            flashIconOff = ta.getDrawable(R.styleable.CaptureView_captureFlashOffIcon)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureTakePictureIcon)) {
            takePictureIcon = ta.getDrawable(R.styleable.CaptureView_captureTakePictureIcon)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFacingIcon)) {
            facingIcon = ta.getDrawable(R.styleable.CaptureView_captureFacingIcon)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureFlashIconSize)) {
            flashIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureFlashIconSize, 0)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureTakePictureIconSize)) {
            takePictureIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureTakePictureIconSize, 0)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFacingIconSize)) {
            facingIconSize = ta.getDimensionPixelSize(R.styleable.CaptureView_captureFacingIconSize, 0)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureFlashIconVisibility)) {
            isShowFlashIcon = ta.getBoolean(R.styleable.CaptureView_captureFlashIconVisibility, true)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureTakePictureIconVisibility)) {
            isShowTakePictureIcon = ta.getBoolean(R.styleable.CaptureView_captureTakePictureIconVisibility, true)
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFacingIconVisibility)) {
            isShowFacingIcon = ta.getBoolean(R.styleable.CaptureView_captureFacingIconVisibility, true)
        }

        if (ta.hasValue(R.styleable.CaptureView_captureFacing)) {
            facing = if (ta.getBoolean(R.styleable.CaptureView_captureFacing, true)) {
                FACING.BACK
            } else {
                FACING.FRONT
            }
        }
        if (ta.hasValue(R.styleable.CaptureView_captureFlash)) {
            flash = if (ta.getBoolean(R.styleable.CaptureView_captureFlash, false)) {
                FLASH.ON
            } else {
                FLASH.OFF
            }
        }
        if (ta.hasValue(R.styleable.CaptureView_captureShape)) {
            shape = if (ta.getBoolean(R.styleable.CaptureView_captureShape, true)) {
                SHAPE.RECTANGLE
            } else {
                SHAPE.CIRCLE
            }
        }

        ta.recycle()
    }

    enum class FACING {
        BACK, FRONT
    }

    enum class FLASH {
        ON, OFF
    }

    enum class SHAPE {
        RECTANGLE, CIRCLE
    }

    interface ICallback {
        fun onTakePictureSuccess(file: File)
        fun onTakePictureFail(exception: Exception)
    }
}
