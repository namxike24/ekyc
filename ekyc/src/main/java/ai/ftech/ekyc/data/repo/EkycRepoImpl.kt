package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.base.invokeApi
import ai.ftech.ekyc.data.source.remote.model.UploadRequest
import ai.ftech.ekyc.data.source.remote.network.invokeFEkycService
import ai.ftech.ekyc.data.source.remote.service.EkycService
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.domain.repo.IEkycRepo
import ai.ftech.ekyc.utils.FileUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EkycRepoImpl : BaseRepo(), IEkycRepo {
    companion object {
        private const val MULTIPART_NAME = "file"
    }

    override fun verifyIdentityPassport(absolutePath: String) {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        service.verifyIdentityPassport(part).invokeApi { headers, body ->

        }
    }

    override fun verifyIdentityFront(absolutePath: String, type: UPLOAD_PHOTO_TYPE) {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }

        val part = convertFileToMultipart(absolutePath)

        service.verifyIdentityFront(part, request).invokeApi { headers, body ->

        }
    }

    override fun verifyIdentityBack(absolutePath: String, type: UPLOAD_PHOTO_TYPE) {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }

        val part = convertFileToMultipart(absolutePath)

        service.verifyIdentityBack(part, request).invokeApi { headers, body ->

        }
    }

    override fun captureFace(absolutePath: String) {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart( absolutePath)

        service.captureFace(part).invokeApi { headers, body ->

        }
    }

    private fun convertFileToMultipart(absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(MULTIPART_NAME, file.name, file.asRequestBody())
    }
}
