package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.utils.FileUtils
import android.util.Log
import com.otaliastudios.cameraview.PictureResult
import java.io.File

class CreateImageFileAction : BaseAction<CreateImageFileAction.CreateRV, String>() {

    override suspend fun execute(rv: CreateRV): String {

        val file = File(FileUtils.getFacePath())
        rv.pictureResult.toFile(file) {
            Log.d("anhnd", "onPictureTaken: ${it?.absolutePath}")
        }

        return file.absolutePath
    }

    class CreateRV(val pictureResult: PictureResult) : RequestValue


}
