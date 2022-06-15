package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import java.io.File

class UploadPhotoAction : BaseAction<UploadPhotoAction.UploadRV, Unit>() {
    override suspend fun execute(rv: UploadRV) {
        val repo = RepositoryFactory.getEkyc()

        val file = File(rv.absolutePath)
        val type = rv.uploadType

        when (rv.uploadType) {
            UPLOAD_PHOTO_TYPE.PASSPORT -> repo.verifyIdentityPassport(file)
            UPLOAD_PHOTO_TYPE.FRONT -> repo.verifyIdentityFront(file, type)
            UPLOAD_PHOTO_TYPE.BACK -> repo.verifyIdentityBack(file, type)
            UPLOAD_PHOTO_TYPE.FACE -> repo.captureFace(file)
        }
    }

    class UploadRV(var absolutePath: String, var uploadType: UPLOAD_PHOTO_TYPE) : RequestValue
}
