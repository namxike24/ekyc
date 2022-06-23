package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE

interface IEkycRepo {
    fun verifyIdentitySSN(absolutePath: String, type: PHOTO_INFORMATION): Boolean
    fun verifyIdentityDriverLicense(absolutePath: String, type: PHOTO_INFORMATION): Boolean
    fun verifyIdentityPassport(absolutePath: String): Boolean
    fun captureFace(absolutePath: String): Boolean
    fun submitInfo(data: String): Boolean
}
