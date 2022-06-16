package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.base.invokeApi
import ai.ftech.ekyc.data.source.remote.base.invokeFEkycService
import ai.ftech.ekyc.data.source.remote.model.UploadRequest
import ai.ftech.ekyc.data.source.remote.service.EkycService
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.domain.repo.IEkycRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EkycRepoImpl : BaseRepo(), IEkycRepo {
    companion object {
        private const val MULTIPART_NAME = "file"
    }

    override fun verifyIdentityPassport(absolutePath: String): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityPassport(part).invokeApi { headers, body ->
            true
        }
    }

    override fun verifyIdentityFront(absolutePath: String, type: UPLOAD_PHOTO_TYPE): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityFront(part, request).invokeApi { headers, body ->
            true
        }
    }

    override fun verifyIdentityBack(absolutePath: String, type: UPLOAD_PHOTO_TYPE): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityBack(part, request).invokeApi { headers, body ->
            true
        }
    }

    override fun captureFace(absolutePath: String): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        return service.captureFace(part).invokeApi { headers, body ->
            true
        }
    }

    private fun convertFileToMultipart(absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(MULTIPART_NAME, file.name, file.asRequestBody())
    }
}
