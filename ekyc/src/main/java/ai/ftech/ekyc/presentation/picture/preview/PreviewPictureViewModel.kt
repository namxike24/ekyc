package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.ekyc.EKYC_PHOTO_TYPE

class PreviewPictureViewModel : BaseViewModel() {
    var ekycType: EKYC_PHOTO_TYPE? = null
    var imagePreviewPath: String? = null
}
