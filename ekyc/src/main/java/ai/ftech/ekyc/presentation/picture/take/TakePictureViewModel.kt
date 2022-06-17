package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.APIException
import ai.ftech.ekyc.domain.action.UploadPhotoAction
import ai.ftech.ekyc.domain.model.EKYC_PHOTO_TYPE
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.utils.FileUtils
import android.util.Log
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
                        Log.d("anhnd", "uploadPhoto: ${it.code}")
                        it.printStackTrace()
                    }
                }.collect {
                    Log.d("anhnd", "uploadPhoto: $it")
                }
            }
        }
    }

    fun getToolbarTitleByEkycType(): String {
        return when (ekycType) {
            EKYC_PHOTO_TYPE.SSN_FRONT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_PHOTO_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_take_front)

            EKYC_PHOTO_TYPE.SSN_BACK,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_take_back)

            EKYC_PHOTO_TYPE.SSN_PORTRAIT,
            EKYC_PHOTO_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_PHOTO_TYPE.PASSPORT_PORTRAIT -> getAppString(R.string.fekyc_take_picture_image_portrait)

            else -> AppConfig.EMPTY_CHAR
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
