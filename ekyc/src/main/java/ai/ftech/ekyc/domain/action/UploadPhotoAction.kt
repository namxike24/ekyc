package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE

class UploadPhotoAction : BaseAction<UploadPhotoAction.UploadRV, Unit>() {
    override suspend fun execute(rv: UploadRV) {
        val repo = RepositoryFactory.getEkyc()

        val path = rv.absolutePath
        val type = rv.uploadType

        when (rv.uploadType) {
            UPLOAD_PHOTO_TYPE.PASSPORT -> repo.verifyIdentityPassport(path)
            UPLOAD_PHOTO_TYPE.FRONT -> repo.verifyIdentityFront(path, type)
            UPLOAD_PHOTO_TYPE.BACK -> repo.verifyIdentityBack(path, type)
            UPLOAD_PHOTO_TYPE.FACE -> repo.captureFace(path)
        }
    }

    class UploadRV(var absolutePath: String, var uploadType: UPLOAD_PHOTO_TYPE) : RequestValue
}
