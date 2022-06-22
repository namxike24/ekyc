package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.asLiveData
import ai.ftech.ekyc.common.action.FEkycActionResult
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_STATUS
import ai.ftech.ekyc.utils.FileUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var currentPhotoType: PHOTO_TYPE? = null
    private var _uploadPhoto = MutableLiveData(FEkycActionResult<UPLOAD_STATUS>())
    val uploadPhoto = _uploadPhoto.asLiveData()
    private var _filePath = MutableLiveData<String>(null)
    val filePath = _filePath.asLiveData()

    init {
        currentPhotoType = EkycStep.getType()
    }

    fun clearUploadPhotoValue() {
        _uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
            this.data = UPLOAD_STATUS.NONE
        }
    }

    fun uploadPhoto(absolutePath: String) {
        viewModelScope.launch {
            currentPhotoType?.let { photoType ->
                if (currentPhotoType != null) {
                    val rv = UploadPhotoAction.UploadRV(absolutePath, photoType, EkycStep.getCurrentStep())
                    UploadPhotoAction().invoke(rv).catch {
                        if (it is APIException) {
                            it.printStackTrace()
                        }
                        _filePath.value = absolutePath
                        _uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
                            this.data = UPLOAD_STATUS.FAIL
                            this.exception = it
                        }
                    }.collect {
                        EkycStep.add(photoType, absolutePath)
                        //check để biết bước tiếp theo đã hoàn thành chưa
                        _uploadPhoto.value = if (EkycStep.isDoneStep()) {
                            FEkycActionResult<UPLOAD_STATUS>().apply {
                                this.data = UPLOAD_STATUS.COMPLETE
                            }

                        } else {
                            FEkycActionResult<UPLOAD_STATUS>().apply {
                                this.data = UPLOAD_STATUS.SUCCESS
                            }
                        }
                    }
                }
            }
        }
    }

    fun getFolderPathByEkycType(): String {
        return when (EkycStep.getCurrentStep()) {
            PHOTO_INFORMATION.FRONT -> FileUtils.getIdentityFrontPath()
            PHOTO_INFORMATION.BACK -> FileUtils.getIdentityBackPath()
            PHOTO_INFORMATION.FACE -> FileUtils.getFacePath()
            PHOTO_INFORMATION.PAGE_NUMBER_2 -> FileUtils.getPassportPath()
        }
    }
}
