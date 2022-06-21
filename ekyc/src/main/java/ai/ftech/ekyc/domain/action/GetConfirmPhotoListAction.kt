package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE
import ai.ftech.ekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo

class GetConfirmPhotoListAction : BaseAction<BaseAction.VoidRequest, List<PhotoConfirmDetailInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<PhotoConfirmDetailInfo> {
        val photoConfirmList = mutableListOf<PhotoInfo>()

//        photoConfirmList.add(getPhotoListFake(CAMERA_TYPE.FRONT))
//        photoConfirmList.add(getPhotoListFake(CAMERA_TYPE.BACK))
//        photoConfirmList.add(getPhotoListFake(CAMERA_TYPE.FACE))

        convertPhotoListToConfirmPhotoList(photoConfirmList)

        return convertPhotoListToConfirmPhotoList(photoConfirmList)
    }

    private fun convertPhotoListToConfirmPhotoList(photoConfirmList: MutableList<PhotoInfo>): List<PhotoConfirmDetailInfo> {

        val photoConfirmDetailInfoList = mutableListOf<PhotoConfirmDetailInfo>()

//        photoConfirmList.forEach { photoInfo ->
//            convertEKYCPhotoTypeToConfirmDetailPhotoType(photoInfo.uploadType)?.let { photoType ->
//                val index = photoConfirmDetailInfoList.indexOfFirst { it.photoType == photoType }
//
//                val photoConfirmDetail = if (index == -1) {
//                    PhotoConfirmDetailInfo().apply {
//                        this.photoType = photoType
//                    }
//                } else {
//                    photoConfirmDetailInfoList[index]
//                }
//
//                photoConfirmDetail.photoList.add(photoInfo)
//
//                if (index == -1) {
//                    photoConfirmDetailInfoList.add(photoConfirmDetail)
//                } else {
//                    photoConfirmDetailInfoList[index] = photoConfirmDetail
//                }
//            }
//        }

        return photoConfirmDetailInfoList
    }

//    private fun convertEKYCPhotoTypeToConfirmDetailPhotoType(ekycType: CAMERA_TYPE?): PHOTO_TYPE? {
//        return when (ekycType) {
//            CAMERA_TYPE.FRONT,
//            CAMERA_TYPE.BACK -> PHOTO_TYPE.SSN
//            CAMERA_TYPE.FACE -> PHOTO_TYPE.PORTRAIT
//            CAMERA_TYPE.PASSPORT -> PHOTO_TYPE.PASSPORT
//            else -> null
//        }
//    }

//    private fun getPhotoListFake(ekycType: CAMERA_TYPE): PhotoInfo {
//        return PhotoInfo().apply {
//            this.url = "/storage/emulated/0/Android/data/ai.ftech.ekyc/files/ftech-ekyc/identity_front.png"
//            this.uploadType = ekycType
//        }
//    }
}
