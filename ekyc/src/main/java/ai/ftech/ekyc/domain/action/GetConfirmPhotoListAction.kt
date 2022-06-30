package ai.ftech.ekyc.domain.action

import ai.ftech.base.common.BaseAction
import ai.ftech.ekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.ekyc.presentation.picture.take.EkycStep

class GetConfirmPhotoListAction : BaseAction<BaseAction.VoidRequest, List<PhotoConfirmDetailInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<PhotoConfirmDetailInfo> {
        val photoConfirmList = mutableListOf<PhotoConfirmDetailInfo>()

        val confirmDetailInfoPapers = PhotoConfirmDetailInfo().apply {
            this.photoType = EkycStep.getType()
            this.photoList = EkycStep.getPaperList()
        }

        val confirmDetailInfoFace = PhotoConfirmDetailInfo().apply {
            this.photoType = null // gán null để group get ra null và tự define ra title của những ảnh ko phải là giấy tờ
            this.photoList = mutableListOf(EkycStep.getPortraitItem())
        }

        photoConfirmList.add(confirmDetailInfoPapers)
        photoConfirmList.add(confirmDetailInfoFace)

        return photoConfirmList
    }
}
