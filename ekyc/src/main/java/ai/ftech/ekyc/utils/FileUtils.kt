package ai.ftech.ekyc.utils

import ai.ftech.ekyc.BuildConfig
import ai.ftech.ekyc.FTechEkycManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    private const val FTECH_EKYC_FOLDER_NAME = "ftech-ekyc"
    private const val PASSPORT = "passport"
    private const val IDENTITY_FRONT = "identity_front"
    private const val IDENTITY_BACK = "identity_back"
    private const val FACE = "face"

    private val imageType = FILE.IMAGE.PNG


    val ROOT_FOLDER = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        FTechEkycManager.getApplicationContext().getExternalFilesDir(null)!!.absolutePath
    } else {
        getRootFolderBelowQ()
    }

    fun bitmapToFile(bitmap: Bitmap, path: String, quality : Int = 100): File? {
        var file: File? = null
        return try {
            file = File(path)
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            val bitmapData: ByteArray = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }
    }

    fun deleteFile(path: String, onScanComplete: (uri: Uri?) -> Unit = {}) {
        val file = File(path)
        scanFileForUri(path, onScanComplete)
        file.delete()
    }

    fun scanFileForUri(path: String, onSuccess: (uri: Uri?) -> Unit = {}, onLoadPath: (path: String?) -> Unit = {}) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaScannerConnection.scanFile(FTechEkycManager.getApplicationContext(), arrayOf(path), null) { _, uri ->
                Handler(Looper.getMainLooper()).post {
                    onSuccess(uri)
                    onLoadPath.invoke(path)
                }
            }
        } else {
            onSuccess(null)
        }
    }

    fun getIdentityPassportPath(): String {
        return "${getFEkycFolder()}/${PASSPORT}.${imageType.extension}"
    }

    fun getIdentityFrontPath(): String {
        return "${getFEkycFolder()}/${IDENTITY_FRONT}.${imageType.extension}"
    }

    fun getIdentityBackPath(): String {
        return "${getFEkycFolder()}/${IDENTITY_BACK}.${imageType.extension}"
    }

    fun getFacePath(): String {
        return "${getFEkycFolder()}/${FACE}.${imageType.extension}"
    }

    private fun getFEkycFolder(): String {
        val path = "${getRootFolder()}/$FTECH_EKYC_FOLDER_NAME"
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    private fun getRootFolder(): String {
        return ROOT_FOLDER
    }

    private fun getRootFolderBelowQ(): String {
        val file = File("${Environment.getExternalStorageDirectory().absolutePath}/${BuildConfig.LIBRARY_PACKAGE_NAME}")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

}

enum class FILE {
    ;

    enum class IMAGE(var extension: String) {
        APNG("apng"),
        AVIF("avif"),
        GIF("gif"),
        JFIF("jfif"),
        JPEG("jpeg"),
        JPG("jpg"),
        PJP("pjp"),
        PJPEG("pjpeg"),
        PNG("png"),
        SVG("svg"),
        WEBP("webp"),
    }
}
