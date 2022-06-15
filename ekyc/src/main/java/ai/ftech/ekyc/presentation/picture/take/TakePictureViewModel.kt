package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.AppConfig
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.presentation.dialog.WARNING_TYPE
import ai.ftech.ekyc.utils.FileUtils

class TakePictureViewModel : BaseViewModel() {
    var ekycType: EKYC_TYPE? = null
    var isFrontFace = false
    var isFlash = false



    fun getToolbarTitleByEkycType(): String {
        return when (ekycType) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.PASSPORT_FRONT -> getAppString(R.string.fekyc_take_picture_take_front)

            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_BACK -> getAppString(R.string.fekyc_take_picture_take_back)

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> getAppString(R.string.fekyc_take_picture_image_portrait)

            else -> AppConfig.EMPTY_CHAR
        }
    }

    fun getFolderPathByEkycType(): String? {
        return when (ekycType) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_FRONT -> FileUtils.getIdentityFrontPath()



            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_BACK -> FileUtils.getIdentityBackPath()

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> FileUtils.getFacePath()


            EKYC_TYPE.PASSPORT_FRONT -> FileUtils.getIdentityPassportPath()

            else -> null
        }
    }

    fun getWarningType(): WARNING_TYPE {
        return when (ekycType!!) {
            EKYC_TYPE.SSN_FRONT,
            EKYC_TYPE.SSN_BACK,
            EKYC_TYPE.DRIVER_LICENSE_FRONT,
            EKYC_TYPE.DRIVER_LICENSE_BACK,
            EKYC_TYPE.PASSPORT_FRONT -> WARNING_TYPE.PAPERS

            EKYC_TYPE.SSN_PORTRAIT,
            EKYC_TYPE.DRIVER_LICENSE_PORTRAIT,
            EKYC_TYPE.PASSPORT_PORTRAIT -> WARNING_TYPE.PORTRAIT
        }
    }
}
