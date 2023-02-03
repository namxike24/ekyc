package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.fekyc.domain.model.ekyc.PHOTO_TYPE

class UploadPhotoAction : BaseAction<UploadPhotoAction.UploadRV, Boolean>() {
    override suspend fun execute(rv: UploadRV): Boolean {
        val repo = RepositoryFactory.getEkyc()

        val photoPath = rv.absolutePath
        val photoType = rv.photoType
        val photoInformation = rv.photoInformation

        return when {
            photoInformation == PHOTO_INFORMATION.FACE -> repo.captureFace(photoPath)

            photoType == PHOTO_TYPE.PASSPORT -> repo.verifyIdentityPassport(photoPath)

            photoType == PHOTO_TYPE.SSN -> repo.verifyIdentitySSN(photoPath, photoInformation)

            photoType == PHOTO_TYPE.DRIVER_LICENSE -> repo.verifyIdentityDriverLicense(photoPath, photoInformation)

            else -> false
        }
    }

    class UploadRV(var absolutePath: String, var photoType: PHOTO_TYPE, var photoInformation: PHOTO_INFORMATION) : RequestValue
}
