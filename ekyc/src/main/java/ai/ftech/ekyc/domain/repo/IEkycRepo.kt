package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import java.io.File

interface IEkycRepo {
    fun verifyIdentityPassport(file: File)
    fun verifyIdentityFront(file: File, type: UPLOAD_PHOTO_TYPE)
    fun verifyIdentityBack(file: File, type: UPLOAD_PHOTO_TYPE)
    fun captureFace(file: File)
}
