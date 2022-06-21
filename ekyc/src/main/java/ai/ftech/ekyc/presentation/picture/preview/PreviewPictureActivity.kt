package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.imageloader.ImageLoaderFactory
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.presentation.dialog.WarningCaptureDialog
import ai.ftech.ekyc.presentation.picture.confirm.ConfirmPictureActivity
import ai.ftech.ekyc.presentation.picture.take.TakePictureActivity
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels

class PreviewPictureActivity : FEkycActivity(R.layout.fekyc_preview_picture_activity) {
    companion object {
        const val SEND_PHOTO_TYPE_KEY = "SEND_PHOTO_TYPE_KEY"
        const val SEND_PREVIEW_IMAGE_KEY = "SEND_PREVIEW_IMAGE_KEY"
    }

    private lateinit var tbvHeader: ToolbarView
    private lateinit var ivImageSrc: ImageView
    private lateinit var btnTakeAgain: Button

    private val viewModel by viewModels<PreviewPictureViewModel>()
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
//        viewModel.ekycType = intent.getSerializableExtra(SEND_EKYC_TYPE_KEY) as? CAMERA_TYPE
        viewModel.photoType = intent.getSerializableExtra(SEND_PHOTO_TYPE_KEY) as? PHOTO_TYPE
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
                showConfirmDialog()
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

    private fun getWarningType(): WARNING_TYPE {
        return when (viewModel.photoType!!) {
            PHOTO_TYPE.SSN_FRONT,
            PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            PHOTO_TYPE.SSN_BACK,
            PHOTO_TYPE.DRIVER_LICENSE_BACK,
            PHOTO_TYPE.PASSPORT_FRONT -> WARNING_TYPE.PAPERS

            PHOTO_TYPE.PORTRAIT -> WARNING_TYPE.PORTRAIT
        }
    }

    private fun getToolbarTitleByEkycType(): String {
        return when (viewModel.photoType!!) {
            PHOTO_TYPE.SSN_FRONT,
            PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            PHOTO_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_take_front)

            PHOTO_TYPE.SSN_BACK,
            PHOTO_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_take_back)

            PHOTO_TYPE.PORTRAIT -> getAppString(R.string.fekyc_take_picture_image_portrait)
        }
    }
}
