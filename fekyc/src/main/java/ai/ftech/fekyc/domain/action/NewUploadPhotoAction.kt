package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.capture.CaptureData
import ai.ftech.fekyc.domain.model.ekyc.CAPTURE_TYPE

class NewUploadPhotoAction : BaseAction<NewUploadPhotoAction.UploadRV, CaptureData>() {

    class UploadRV(
        var absolutePath: String,
        var transactionId: String,
        var orientation: CAPTURE_TYPE
    ) : RequestValue

    override suspend fun execute(rv: UploadRV): CaptureData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        val orientationValue = rv.orientation.value
        return if (orientationValue == null) {
            repo.captureFace(rv.transactionId, rv.absolutePath)
        } else {
            repo.capturePhoto(rv.transactionId, orientationValue, rv.absolutePath)
        }
    }
}