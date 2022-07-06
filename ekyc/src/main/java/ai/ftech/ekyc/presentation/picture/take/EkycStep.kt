package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
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

    fun add(photoType: PHOTO_TYPE, path: String) {
        val photoInfo = PhotoInfo().apply {
            this.photoType = photoType
            this.url = path
            this.photoInformation = getCurrentStep()
        }

        stepList.add(photoInfo)
    }

    fun getPaperList(): MutableList<PhotoInfo> {
        if (getType() == PHOTO_TYPE.SSN || getType() == PHOTO_TYPE.DRIVER_LICENSE) {
            if (stepList.size > 2) {
                val paperFront = stepList[0]
                val paperBack = stepList[1]
                return mutableListOf(paperFront, paperBack)
            }
        } else if (getType() == PHOTO_TYPE.PASSPORT) {
            val passportItem = stepList.firstOrNull()
            if (passportItem != null) {
                return mutableListOf(passportItem)
            }
        }
        return mutableListOf()
    }

    fun getPortraitItem(): PhotoInfo {
        return stepList.last()
    }

    fun clear() {
        stepList.clear()
    }

    private fun isTypePassport(): Boolean {
        return photoType == PHOTO_TYPE.PASSPORT
    }
}
