package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE

class PreviewPictureViewModel : BaseViewModel() {
    var photoType: PHOTO_TYPE? = null
    var imagePreviewPath: String? = null
}
