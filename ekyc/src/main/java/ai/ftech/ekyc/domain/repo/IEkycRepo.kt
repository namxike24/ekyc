package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.ekyc.PHOTO_TYPE

interface IEkycRepo {
    fun verifyIdentityPassport(absolutePath: String): Boolean
    fun verifyIdentityFront(absolutePath: String, type: PHOTO_TYPE): Boolean
    fun verifyIdentityBack(absolutePath: String, type: PHOTO_TYPE): Boolean
    fun captureFace(absolutePath: String): Boolean
}
