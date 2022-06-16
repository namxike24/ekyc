package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.base.invokeApi
import ai.ftech.ekyc.data.source.remote.model.UploadRequest
import ai.ftech.ekyc.data.source.remote.network.invokeFEkycService
import ai.ftech.ekyc.data.source.remote.service.EkycService
import ai.ftech.ekyc.domain.model.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.domain.repo.IEkycRepo
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityFront(part, convertToRequestBody(type.type)).invokeApi { headers, body ->
            true
        }
    }

    override fun verifyIdentityBack(absolutePath: String, type: UPLOAD_PHOTO_TYPE): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityBack(part, convertToRequestBody(type.type)).invokeApi { headers, body ->
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

    private fun convertToRequestBody(field: String): RequestBody {
        return field.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
