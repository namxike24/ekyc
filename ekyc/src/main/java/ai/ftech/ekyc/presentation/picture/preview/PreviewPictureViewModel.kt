package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.EKYC_TYPE

class PreviewPictureViewModel : BaseViewModel() {
    var ekycType: EKYC_TYPE? = null
    var imagePreviewPath: String? = null
}
