package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.base.invokeApi
import ai.ftech.ekyc.data.source.remote.base.invokeFEkycService
import ai.ftech.ekyc.data.source.remote.service.EkycService
import ai.ftech.ekyc.domain.model.ekyc.UPLOAD_PHOTO_TYPE
import ai.ftech.ekyc.domain.repo.IEkycRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EkycRepoImpl : BaseRepo(), IEkycRepo {
    companion object {
        private const val PART_FILE = "file"
        private const val PART_FIELD_TYPE = "type"
    }

    override fun verifyIdentityPassport(absolutePath: String): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        return service.verifyIdentityPassport(part).invokeApi { _, body ->
            true
        }
    }

    override fun verifyIdentityFront(absolutePath: String, type: UPLOAD_PHOTO_TYPE): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        val map = hashMapOf(PART_FIELD_TYPE to convertToRequestBody(type.type))

        return service.verifyIdentityFront(part, map).invokeApi { _, body ->
            true
        }
    }

    override fun verifyIdentityBack(absolutePath: String, type: UPLOAD_PHOTO_TYPE): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        val map = hashMapOf(PART_FIELD_TYPE to convertToRequestBody(type.type))

        return service.verifyIdentityBack(part, map).invokeApi { _, body ->
            true
        }
    }

    override fun captureFace(absolutePath: String): Boolean {
        val service = invokeFEkycService(EkycService::class.java)

        val part = convertFileToMultipart(absolutePath)

        return service.captureFace(part).invokeApi { _, body ->
            true
        }
    }

    private fun convertFileToMultipart(absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(PART_FILE, file.name, file.asRequestBody())
    }

    private fun convertToRequestBody(field: String): RequestBody {
        return field.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
