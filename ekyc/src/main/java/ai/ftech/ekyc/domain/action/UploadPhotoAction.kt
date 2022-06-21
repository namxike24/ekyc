package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE

class UploadPhotoAction : BaseAction<UploadPhotoAction.UploadRV, Boolean>() {
    override suspend fun execute(rv: UploadRV): Boolean {
        val repo = RepositoryFactory.getEkyc()

        val path = rv.absolutePath
        val type = rv.photoType

        return when (rv.photoType) {
            PHOTO_TYPE.PASSPORT_FRONT -> repo.verifyIdentityPassport(path)

            PHOTO_TYPE.SSN_FRONT,
            PHOTO_TYPE.DRIVER_LICENSE_FRONT -> repo.verifyIdentityFront(path, type)


            PHOTO_TYPE.SSN_BACK,
            PHOTO_TYPE.DRIVER_LICENSE_BACK -> repo.verifyIdentityBack(path, type)

            PHOTO_TYPE.PORTRAIT -> repo.captureFace(path)
        }
    }

    class UploadRV(var absolutePath: String, var photoType: PHOTO_TYPE) : RequestValue
}
