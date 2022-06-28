package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.getAppString
import ai.ftech.ekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.take.EkycStep
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels

class PreviewPictureActivity : FEkycActivity(R.layout.fekyc_preview_picture_activity) {
    companion object {
        const val SEND_PREVIEW_IMAGE_KEY = "SEND_PREVIEW_IMAGE_KEY"
        const val SEND_MESSAGE_KEY = "SEND_MESSAGE_KEY"
    }

    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivImageSrc: ImageView
    private lateinit var tvMessage: TextView
    private lateinit var btnTakeAgain: Button

    private val viewModel by viewModels<PreviewPictureViewModel>()
    private val imageLoader = ImageLoaderFactory.glide()

    override fun onResume() {
        super.onResume()
        if (warningDialog == null) {
            val type = getWarningType()
            warningDialog = WarningCaptureDialog(type)
        }
    }

    override fun onPause() {
        super.onPause()
        warningDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        viewModel.imagePreviewPath = intent.getStringExtra(SEND_PREVIEW_IMAGE_KEY)
        viewModel.message = intent.getStringExtra(SEND_MESSAGE_KEY)
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvPreviewPictureHeader)
        tvMessage = findViewById(R.id.tvPreviewPictureErrorMessage)
        ivImageSrc = findViewById(R.id.ivPreviewPictureImageSrc)
        btnTakeAgain = findViewById(R.id.btnPreviewPictureTakeAgain)

        tbvHeader.setTitle(getToolbarTitleByEkycType())
        tvMessage.text = viewModel.message

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                onBackPressed()
            }

            override fun onRightIconClick() {
                warningDialog?.showDialog(supportFragmentManager, warningDialog!!::class.java.simpleName)
            }
        })

        imageLoader.loadImage(activity = this, url = viewModel.imagePreviewPath, view = ivImageSrc, ignoreCache = true)

        btnTakeAgain.setOnSafeClick {
            finish()
        }
    }

    private fun getWarningType(): WARNING_TYPE {
        return when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FRONT,
            PHOTO_INFORMATION.BACK,
            PHOTO_INFORMATION.PAGE_NUMBER_2 -> WARNING_TYPE.PAPERS
            PHOTO_INFORMATION.FACE -> WARNING_TYPE.PORTRAIT
        }
    }

    private fun getToolbarTitleByEkycType(): String {
        return when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FRONT -> getAppString(R.string.fekyc_take_picture_take_front)
            PHOTO_INFORMATION.BACK -> getAppString(R.string.fekyc_take_picture_take_back)
            PHOTO_INFORMATION.FACE -> getAppString(R.string.fekyc_take_picture_image_portrait)
            PHOTO_INFORMATION.PAGE_NUMBER_2 -> getAppString(R.string.fekyc_take_picture_take_passport)
        }
    }
}
