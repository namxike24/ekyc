package ai.ftech.ekyc.presentation.picture.preview

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.presentation.picture.take.EkycStep

class PreviewPictureViewModel : BaseViewModel() {
    var photoType: PHOTO_INFORMATION? = EkycStep.getCurrentStep()
    var imagePreviewPath: String? = null
    var message: String? = null
}
