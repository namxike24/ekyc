package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.capture.CaptureData

class NewUploadPhotoAction : BaseAction<NewUploadPhotoAction.UploadRV, CaptureData>() {

    class UploadRV(
        var absolutePath: String,
        var transactionId: String,
        var orientation: String? = null
    ) : RequestValue

    override suspend fun execute(rv: UploadRV): CaptureData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return if (rv.orientation == null) {
            repo.captureFace(rv.transactionId, rv.absolutePath)
        } else {
            repo.capturePhoto(rv.transactionId, rv.orientation!!, rv.absolutePath)
        }
    }
}