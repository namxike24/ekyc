package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.asLiveData
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.utils.FileUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var currentPhotoType: PHOTO_TYPE? = null
    private var _uploadPhoto = MutableLiveData(false)
    val uploadPhoto = _uploadPhoto.asLiveData()

    init {
        currentPhotoType = EkycStep.getCurrentStep()
    }

    fun clearUploadPhotoValue() {
        _uploadPhoto.value = false
    }

    fun uploadPhoto(absolutePath: String) {
        viewModelScope.launch {
            if (currentPhotoType != null) {
                val rv = UploadPhotoAction.UploadRV(absolutePath, currentPhotoType!!)
                UploadPhotoAction().invoke(rv).catch {
                    if (it is APIException) {
                        it.printStackTrace()
                    }
                    _uploadPhoto.value = null
                }.collect {
                    _uploadPhoto.value = it

//                    if (!EkycStep.isDoneStep()) {
//                        cameraType = if (EkycStep.getNextStep() == null) {
//                            CAMERA_TYPE.FRONT
//                        } else {
//                            EkycStep.getNextStep()
//                        }
//                    }


//                    EkycStep.add()
                }
            }
        }
    }

    fun getFolderPathByEkycType(): String? {
        return when (currentPhotoType) {
            PHOTO_TYPE.SSN_FRONT,
            PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            PHOTO_TYPE.PASSPORT_FRONT -> FileUtils.getIdentityFrontPath()

            PHOTO_TYPE.SSN_BACK,
            PHOTO_TYPE.DRIVER_LICENSE_BACK -> FileUtils.getIdentityBackPath()

            PHOTO_TYPE.PORTRAIT -> FileUtils.getFacePath()
            else -> null
        }
    }
}
