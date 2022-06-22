package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.ekyc.PHOTO_INFORMATION
import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE

interface IEkycRepo {
    fun verifyIdentityPassport(absolutePath: String): Boolean
    fun verifyIdentity(absolutePath: String, type: PHOTO_INFORMATION): Boolean
    fun captureFace(absolutePath: String): Boolean
}
