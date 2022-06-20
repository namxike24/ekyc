package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE

object EkycStep {
    private var stepList = mutableListOf<PhotoInfo>()

    fun isDoneStep(): Boolean {
        return stepList.size == 3
    }

    fun getNextStep(): UPLOAD_PHOTO_TYPE? {
        if (stepList.size in 1..3) {
            stepList.forEachIndexed { index, value ->
                if (index == stepList.size - 1) {
                    return getNextStepUploadType(value)
                }
            }
        } else if (stepList.size > 3) {
            return UPLOAD_PHOTO_TYPE.DONE
        }
        return null
    }

    fun add(photoType: PHOTO_TYPE, uploadType: UPLOAD_PHOTO_TYPE) {
        val photoInfo = PhotoInfo().apply {
            this.photoType = photoType
            this.uploadType = uploadType
        }

        stepList.add(photoInfo)
    }

    fun clear() {
        stepList.clear()
    }

    private fun getNextStepUploadType(photoInfo: PhotoInfo): UPLOAD_PHOTO_TYPE? {
        return when (photoInfo.uploadType) {
            UPLOAD_PHOTO_TYPE.FRONT -> UPLOAD_PHOTO_TYPE.BACK
            UPLOAD_PHOTO_TYPE.BACK -> UPLOAD_PHOTO_TYPE.FACE
            UPLOAD_PHOTO_TYPE.PASSPORT -> UPLOAD_PHOTO_TYPE.FACE
            UPLOAD_PHOTO_TYPE.FACE -> UPLOAD_PHOTO_TYPE.DONE
            else -> UPLOAD_PHOTO_TYPE.FRONT
        }
    }
}
