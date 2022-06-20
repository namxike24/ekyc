package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.ekyc.EKYC_PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.utils.FileUtils
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var ekycType: EKYC_PHOTO_TYPE? = null

    fun uploadPhoto(absolutePath: String) {
        viewModelScope.launch {
            val uploadType = when (ekycType) {

                EKYC_PHOTO_TYPE.PASSPORT_FRONT -> UPLOAD_PHOTO_TYPE.PASSPORT

                EKYC_PHOTO_TYPE.SSN_FRONT,
                EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT -> UPLOAD_PHOTO_TYPE.FRONT

                EKYC_PHOTO_TYPE.SSN_BACK,
                EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> UPLOAD_PHOTO_TYPE.FRONT

                EKYC_PHOTO_TYPE.SSN_PORTRAIT,
                EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
                EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT -> UPLOAD_PHOTO_TYPE.FACE

                else -> null
            }

            if (uploadType != null) {
                val rv = UploadPhotoAction.UploadRV(absolutePath, uploadType)
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
            EKYC_PHOTO_TYPE.SSN_FRONT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT -> FileUtils.getIdentityFrontPath()

            EKYC_PHOTO_TYPE.SSN_BACK,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> FileUtils.getIdentityBackPath()

            EKYC_PHOTO_TYPE.SSN_PORTRAIT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT -> FileUtils.getFacePath()

            EKYC_PHOTO_TYPE.PASSPORT_FRONT -> FileUtils.getIdentityPassportPath()

            else -> null
        }
    }
}
