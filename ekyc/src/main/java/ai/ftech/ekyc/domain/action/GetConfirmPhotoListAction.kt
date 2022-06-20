package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE

class GetConfirmPhotoListAction : BaseAction<BaseAction.VoidRequest, List<PhotoConfirmDetailInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<PhotoConfirmDetailInfo> {

        val photoConfirmList = mutableListOf<PhotoInfo>()

        photoConfirmList.addAll(getPhotoListFake(UPLOAD_PHOTO_TYPE.FRONT))
        photoConfirmList.addAll(getPhotoListFake(UPLOAD_PHOTO_TYPE.BACK))
        photoConfirmList.addAll(getPhotoListFake(UPLOAD_PHOTO_TYPE.FACE))

        convertPhotoListToConfirmPhotoList(photoConfirmList)

        return convertPhotoListToConfirmPhotoList(photoConfirmList)
    }

    private fun convertPhotoListToConfirmPhotoList(photoConfirmList: MutableList<PhotoInfo>): List<PhotoConfirmDetailInfo> {

        val photoConfirmDetailInfoList = mutableListOf<PhotoConfirmDetailInfo>()

        photoConfirmList.forEach { photoInfo ->
            convertEKYCPhotoTypeToConfirmDetailPhotoType(photoInfo.ekycType)?.let { photoType ->
                val index = photoConfirmDetailInfoList.indexOfFirst { it.photoType == photoType }

                val photoConfirmDetail = if (index == -1) {
                    PhotoConfirmDetailInfo().apply {
                        this.photoType = photoType
                    }
                } else {
                    photoConfirmDetailInfoList[index]
                }

                photoConfirmDetail.photoList.add(photoInfo)

                if (index == -1) {
                    photoConfirmDetailInfoList.add(photoConfirmDetail)
                } else {
                    photoConfirmDetailInfoList[index] = photoConfirmDetail
                }
            }
        }

        return photoConfirmDetailInfoList
    }

    private fun convertEKYCPhotoTypeToConfirmDetailPhotoType(ekycType: UPLOAD_PHOTO_TYPE?): PHOTO_TYPE? {
        return when (ekycType) {
            UPLOAD_PHOTO_TYPE.FRONT,
            UPLOAD_PHOTO_TYPE.BACK -> PHOTO_TYPE.SSN
            UPLOAD_PHOTO_TYPE.FACE -> PHOTO_TYPE.PORTRAIT
            UPLOAD_PHOTO_TYPE.PASSPORT -> PHOTO_TYPE.PASSPORT
            else -> null
        }
    }

    private fun getPhotoListFake(ekycType: UPLOAD_PHOTO_TYPE, size: Int = 1): List<PhotoInfo> {
        val photoList = mutableListOf<PhotoInfo>()

        for (i in 0 until size) {
            val photoInfo = PhotoInfo().apply {
                this.url = "/storage/emulated/0/Android/data/ai.ftech.ekyc/files/ftech-ekyc/identity_front.png"
                this.ekycType = ekycType
            }

            photoList.add(photoInfo)
        }

        return photoList
    }
}
