package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import ai.ftech.ekyc.domain.model.PhotoInfo
import ai.ftech.ekyc.presentation.picture.confirm.ConfirmPictureAdapter

class GetConfirmPhotoListAction : BaseAction<BaseAction.VoidRequest, List<Any>>() {
    override suspend fun execute(rv: VoidRequest): List<Any> {

        val photoConfirmList = mutableListOf<Any>()

        val titlePapers = ConfirmPictureAdapter.SingleTitle().apply {
            this.title = getAppString(R.string.fekyc_confirm_picture_capture_two_face_papers)
        }

        val titlePortrait = ConfirmPictureAdapter.SingleTitle().apply {
            this.title = getAppString(R.string.fekyc_confirm_picture_portrait_myself)
        }

        photoConfirmList.add(titlePapers)
        photoConfirmList.addAll(getPhotoListFake(EKYC_TYPE.SSN_FRONT, 2))
        photoConfirmList.add(titlePortrait)
        photoConfirmList.addAll(getPhotoListFake(EKYC_TYPE.SSN_PORTRAIT))

        return photoConfirmList
    }

    private fun getPhotoListFake(ekycType: EKYC_TYPE, size: Int = 1): List<PhotoInfo> {
        val photoList = mutableListOf<PhotoInfo>()

        for (i in 0 until size) {
            val photoInfo = PhotoInfo().apply {
                this.url = ""
                this.ekycType = ekycType
                this.isValid = true
            }
            photoList.add(photoInfo)
        }

        return photoList
    }
}
