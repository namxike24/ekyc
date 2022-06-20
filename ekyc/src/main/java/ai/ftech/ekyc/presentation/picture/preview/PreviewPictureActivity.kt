package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.confirm.ConfirmPictureActivity
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels

class PreviewPictureActivity : FEkycActivity(R.layout.fekyc_preview_picture_activity) {
    companion object {
        const val SEND_EKYC_TYPE_KEY = "SEND_EKYC_TYPE_KEY"
        const val SEND_PREVIEW_IMAGE_KEY = "SEND_PREVIEW_IMAGE_KEY"
    }

    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivImageSrc: ImageView
    private lateinit var btnTakeAgain: Button

    private val viewModel by viewModels<PreviewPictureViewModel>()
    private var warningDialog: WarningCaptureDialog? = null
    private val imageLoader = ImageLoaderFactory.glide()


    override fun onResume() {
        super.onResume()
        if (warningDialog == null) {
            val type = getWarningType()
            if (type != null) {
                warningDialog = WarningCaptureDialog(type)
            }
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
        viewModel.ekycType = intent.getSerializableExtra(SEND_EKYC_TYPE_KEY) as? UPLOAD_PHOTO_TYPE
        viewModel.imagePreviewPath = intent.getStringExtra(SEND_PREVIEW_IMAGE_KEY)
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvPreviewPictureHeader)
        ivImageSrc = findViewById(R.id.ivPreviewPictureImageSrc)
        btnTakeAgain = findViewById(R.id.btnPreviewPictureTakeAgain)

        tbvHeader.setTitle(getToolbarTitleByEkycType())

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                val dialog = ConfirmDialog.Builder()
                    .setTitle(getAppString(R.string.fekyc_confirm_notification_title))
                    .setContent(getAppString(R.string.fekyc_confirm_notification_content))
                    .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
                    .setRightTitle(getAppString(R.string.fekyc_confirm_stay))
                    .build()
                dialog.listener = object : ConfirmDialog.IListener {
                    override fun onLeftClick() {
                        finish()
                    }

                    override fun onRightClick() {
                        dialog.dismissDialog()
                    }
                }
                dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
            }

            override fun onRightIconClick() {
                warningDialog?.showDialog(supportFragmentManager, warningDialog!!::class.java.simpleName)
            }
        })

        imageLoader.loadImage(activity = this, url = viewModel.imagePreviewPath, view = ivImageSrc, ignoreCache = true)

        btnTakeAgain.setOnSafeClick {
            navigateTo(ConfirmPictureActivity::class.java)
        }
    }

    private fun getToolbarTitleByEkycType(): String {
        return when (viewModel.ekycType) {
            UPLOAD_PHOTO_TYPE.FRONT,
            UPLOAD_PHOTO_TYPE.PASSPORT -> getAppString(R.string.fekyc_take_picture_take_front)

            UPLOAD_PHOTO_TYPE.BACK -> getAppString(R.string.fekyc_take_picture_take_back)

            UPLOAD_PHOTO_TYPE.FACE -> getAppString(R.string.fekyc_take_picture_image_portrait)

            else -> AppConfig.EMPTY_CHAR
        }
    }

    private fun getWarningType(): WARNING_TYPE? {
        return when (viewModel.ekycType) {
            UPLOAD_PHOTO_TYPE.FRONT,
            UPLOAD_PHOTO_TYPE.BACK,
            UPLOAD_PHOTO_TYPE.PASSPORT -> WARNING_TYPE.PAPERS

            UPLOAD_PHOTO_TYPE.FACE -> WARNING_TYPE.PORTRAIT

            else -> null
        }
    }
}
