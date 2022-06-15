package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import java.io.File

interface IEkycRepo {
    fun verifyIdentityPassport(absolutePath:String)
    fun verifyIdentityFront(absolutePath:String, type: UPLOAD_PHOTO_TYPE)
    fun verifyIdentityBack(absolutePath:String, type: UPLOAD_PHOTO_TYPE)
    fun captureFace(absolutePath:String)
}
