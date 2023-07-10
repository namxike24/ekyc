package ai.ftech.fekyc.presentation.picture.take

import ai.ftech.fekyc.base.common.BaseViewModel
import ai.ftech.fekyc.common.action.FEkycActionResult
import ai.ftech.fekyc.common.onException
import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.domain.action.NewUploadPhotoAction
import ai.ftech.fekyc.domain.action.UploadPhotoAction
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.fekyc.domain.model.ekyc.UPLOAD_STATUS
import ai.ftech.fekyc.utils.FileUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var currentPhotoType: PHOTO_TYPE? = null

    var uploadPhoto = MutableLiveData(FEkycActionResult<UPLOAD_STATUS>())
        private set

    var filePath: String? = null
        private set

    init {
        currentPhotoType = EkycStep.getType()
    }

    fun clearUploadPhotoValue() {
        uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
            this.data = UPLOAD_STATUS.NONE
        }
    }

    fun uploadPhoto(absolutePath: String, orientation: String?) {
        viewModelScope.launch {
            currentPhotoType?.let { photoType ->
                if (currentPhotoType != null) {
                    val rv2 = NewUploadPhotoAction.UploadRV(absolutePath = absolutePath, transactionId = "2e5912a0-0ead-4fda-8849-11540b9b68ff", orientation = orientation)
                    NewUploadPhotoAction().invoke(rv2).onException {
                        if (it is APIException) {
                            it.printStackTrace()
                        }
                        filePath = absolutePath
                        uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
                            this.exception = it
                            this.data = UPLOAD_STATUS.FAIL
                        }
                    }.collect{
                        uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
                            this.data = UPLOAD_STATUS.SUCCESS
                        }
                    }
                    /*val rv = UploadPhotoAction.UploadRV(absolutePath, photoType, EkycStep.getCurrentStep())
                    UploadPhotoAction().invoke(rv).onException {
                        if (it is APIException) {
                            it.printStackTrace()
                        }
                        filePath = absolutePath
                        uploadPhoto.value = FEkycActionResult<UPLOAD_STATUS>().apply {
                            this.exception = it
                            this.data = UPLOAD_STATUS.FAIL
                        }
                    }.collect {
                        EkycStep.add(photoType, absolutePath)
                        //check để biết bước tiếp theo đã hoàn thành chưa
                        uploadPhoto.value = if (EkycStep.isDoneStep()) {
                            FEkycActionResult<UPLOAD_STATUS>().apply {
                                this.data = UPLOAD_STATUS.COMPLETE
                            }

                        } else {
                            FEkycActionResult<UPLOAD_STATUS>().apply {
                                this.data = UPLOAD_STATUS.SUCCESS
                            }
                        }
                    }*/
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
