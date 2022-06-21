package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo

object EkycStep {
    private const val PASSPORT_STEP_COUNT = 2
    private const val PAPERS_STEP_COUNT = 3
    private var stepList = mutableListOf<PhotoInfo>()
    private var photoType: PHOTO_TYPE = PHOTO_TYPE.SSN

    fun setType(type: PHOTO_TYPE) {
        photoType = type
    }

    fun getType() = photoType

    fun isDoneStep(): Boolean {
        return stepList.size == if (isTypePassport()) PASSPORT_STEP_COUNT else PAPERS_STEP_COUNT
    }

//    fun getCurrentStep(): PHOTO_TYPE? {
//        return stepList.last().photoType
//    }

    fun getCurrentStep(): PHOTO_INFORMATION {
        return if(isTypePassport()) {
            when (stepList.size) {
                0 -> PHOTO_INFORMATION.PAGE_NUMBER_2
                1 -> PHOTO_INFORMATION.FACE
                else -> PHOTO_INFORMATION.PAGE_NUMBER_2
            }
        } else {
            when (stepList.size) {
                0 -> PHOTO_INFORMATION.FRONT
                1 -> PHOTO_INFORMATION.BACK
                2 -> PHOTO_INFORMATION.FACE
                else -> PHOTO_INFORMATION.FRONT
            }
        }
    }

//    fun getNextStep(): PHOTO_TYPE? {
//        if (stepList.size > 0) {
//            val lastItem = stepList.last()
//            val stepCount = if (lastItem.photoType == PHOTO_TYPE.PASSPORT) {
//                PASSPORT_STEP_COUNT
//            } else {
//                PAPERS_STEP_COUNT
//            }
//            if (stepList.size in 1..stepCount) {
//                return getNextStepUploadType(lastItem)
//            }
//        }
//        return null
//    }

    fun add(photoType: PHOTO_TYPE, path: String) {
        val photoInfo = PhotoInfo().apply {
            this.photoType = photoType
            this.url = path
        }

        stepList.add(photoInfo)
    }

    fun clear() {
        stepList.clear()
    }

    private fun isTypePassport(): Boolean {
        return photoType == PHOTO_TYPE.PASSPORT
    }

//    private fun getNextStepUploadType(photoInfo: PhotoInfo): PHOTO_TYPE? {
//        return when (photoInfo.photoType) {
//            PHOTO_TYPE.SSN_FRONT -> PHOTO_TYPE.SSN_BACK
//
//
//            PHOTO_TYPE.DRIVER_LICENSE_FRONT -> PHOTO_TYPE.DRIVER_LICENSE_BACK
//
//            PHOTO_TYPE.SSN_BACK,
//            PHOTO_TYPE.DRIVER_LICENSE_BACK,
//            PHOTO_TYPE.PASSPORT_FRONT -> PHOTO_TYPE.PORTRAIT
//
//            PHOTO_TYPE.PORTRAIT -> null
//            else -> null
//        }
//    }
}
