package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.utils.FileUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var uploadType: UPLOAD_PHOTO_TYPE? = UPLOAD_PHOTO_TYPE.FRONT
    var photoType: PHOTO_TYPE? = null
    private var _uploadPhoto = MutableLiveData(false)
    val uploadPhoto = _uploadPhoto

    fun uploadPhoto(absolutePath: String) {
        viewModelScope.launch {
            if (uploadType != null) {
                val rv = UploadPhotoAction.UploadRV(absolutePath, uploadType!!)
                UploadPhotoAction().invoke(rv).catch {
                    if (it is APIException) {
                        it.printStackTrace()
                    }
                }.collect {
                    _uploadPhoto.value = it
                    if (!EkycStep.isDoneStep()) {
                        uploadType = if (EkycStep.getNextStep() == null) {
                            UPLOAD_PHOTO_TYPE.FRONT
                        } else {
                            EkycStep.getNextStep()
                        }
                    }
                    EkycStep.add(photoType = photoType!!, uploadType = uploadType!!)
                }
            }
        }
    }

    fun getFolderPathByEkycType(): String? {
        return when (uploadType) {
            UPLOAD_PHOTO_TYPE.FRONT -> FileUtils.getIdentityFrontPath()
            UPLOAD_PHOTO_TYPE.BACK -> FileUtils.getIdentityBackPath()
            UPLOAD_PHOTO_TYPE.FACE -> FileUtils.getFacePath()
            UPLOAD_PHOTO_TYPE.PASSPORT -> FileUtils.getIdentityPassportPath()
            else -> null
        }
    }
}
