package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.base.invokeApi
import ai.ftech.ekyc.data.source.remote.model.UploadRequest
import ai.ftech.ekyc.data.source.remote.network.invokeFEkycService
import ai.ftech.ekyc.data.source.remote.service.EkycService
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.domain.repo.IEkycRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EkycImpl : BaseRepo(), IEkycRepo {
    companion object {
        private const val MULTIPART_NAME = "file"
    }

    override fun verifyIdentityPassport(file: File) {
        val service = invokeFEkycService(EkycService::class.java)

        service.verifyIdentityPassport(getMultipart(file)).invokeApi { headers, body ->

        }
    }

    override fun verifyIdentityFront(file: File, type: UPLOAD_PHOTO_TYPE) {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }
        service.verifyIdentityFront(getMultipart(file), request).invokeApi { headers, body ->

        }
    }

    override fun verifyIdentityBack(file: File, type: UPLOAD_PHOTO_TYPE) {
        val service = invokeFEkycService(EkycService::class.java)

        val request = UploadRequest().apply {
            this.type = type.type
        }
        service.verifyIdentityBack(getMultipart(file), request).invokeApi { headers, body ->

        }
    }

    override fun captureFace(file: File) {
        val service = invokeFEkycService(EkycService::class.java)

        service.captureFace(getMultipart(file)).invokeApi { headers, body ->

        }
    }

    private fun getMultipart(file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(MULTIPART_NAME, file.name, file.asRequestBody())
    }
}
