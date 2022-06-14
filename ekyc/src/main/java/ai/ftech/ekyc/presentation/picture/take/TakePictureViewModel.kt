package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.model.EKYC_TYPE

class TakePictureViewModel : BaseViewModel() {
    var ekycType: EKYC_TYPE? = null
    var isFrontFace = false
    var isFlash = false
}
