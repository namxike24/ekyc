package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.domain.model.EKYC_PHOTO_TYPE
import ai.ftech.ekyc.domain.model.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.PhotoInfo

class GetConfirmPhotoListAction : BaseAction<BaseAction.VoidRequest, List<PhotoConfirmDetailInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<PhotoConfirmDetailInfo> {

        val photoConfirmList = mutableListOf<PhotoInfo>()

        photoConfirmList.addAll(getPhotoListFake(EKYC_PHOTO_TYPE.SSN_FRONT))
        photoConfirmList.addAll(getPhotoListFake(EKYC_PHOTO_TYPE.SSN_BACK))
        photoConfirmList.addAll(getPhotoListFake(EKYC_PHOTO_TYPE.PORTRAIT))

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

    private fun convertEKYCPhotoTypeToConfirmDetailPhotoType(ekycType: EKYC_PHOTO_TYPE?): PhotoConfirmDetailInfo.PHOTO_TYPE? {
        return when (ekycType) {
            EKYC_PHOTO_TYPE.SSN_FRONT, EKYC_PHOTO_TYPE.SSN_BACK -> {
                PhotoConfirmDetailInfo.PHOTO_TYPE.SSN
            }

            EKYC_PHOTO_TYPE.PORTRAIT -> {
                PhotoConfirmDetailInfo.PHOTO_TYPE.PORTRAIT
            }

            EKYC_PHOTO_TYPE.DRIVER_LICENSE_FRONT, EKYC_PHOTO_TYPE.DRIVER_LICENSE_BACK -> {
                PhotoConfirmDetailInfo.PHOTO_TYPE.DRIVER_LICENSE
            }

            EKYC_PHOTO_TYPE.PASSPORT_FRONT -> {
                PhotoConfirmDetailInfo.PHOTO_TYPE.PASSPORT
            }
            else -> {
                null
            }
        }
    }

    private fun getPhotoListFake(ekycType: EKYC_PHOTO_TYPE, size: Int = 1): List<PhotoInfo> {
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
