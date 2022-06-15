package ai.ftech.ekyc.utils

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object FileUtils {

    fun convertFileToMultipart(name: String, absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(name, file.name, file.asRequestBody())
    }
}
