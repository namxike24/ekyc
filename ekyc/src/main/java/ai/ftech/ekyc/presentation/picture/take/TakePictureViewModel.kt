package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.utils.FileUtils
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var ekycType: UPLOAD_PHOTO_TYPE? = null

    fun uploadPhoto(absolutePath: String) {
        viewModelScope.launch {
            if (ekycType != null) {
                val rv = UploadPhotoAction.UploadRV(absolutePath, ekycType!!)
                UploadPhotoAction().invoke(rv).catch {
                    if (it is APIException) {
                        it.printStackTrace()
                    }
                }.collect {

                }
            }
        }
    }

    fun getFolderPathByEkycType(): String? {
        return when (ekycType) {
            UPLOAD_PHOTO_TYPE.FRONT -> FileUtils.getIdentityFrontPath()
            UPLOAD_PHOTO_TYPE.BACK -> FileUtils.getIdentityBackPath()
            UPLOAD_PHOTO_TYPE.FACE -> FileUtils.getFacePath()
            UPLOAD_PHOTO_TYPE.PASSPORT -> FileUtils.getIdentityPassportPath()
            else -> null
        }
    }
}
