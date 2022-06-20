package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE

class PreviewPictureViewModel : BaseViewModel() {
    var ekycType: UPLOAD_PHOTO_TYPE? = null
    var imagePreviewPath: String? = null
}
