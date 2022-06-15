package ai.ftech.ekyc.presentation.picture.take

import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.ekyc.domain.action.CreateImageFileAction
import ai.ftech.ekyc.domain.model.EKYC_TYPE
import androidx.lifecycle.viewModelScope
import com.otaliastudios.cameraview.PictureResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TakePictureViewModel : BaseViewModel() {
    var ekycType: EKYC_TYPE? = null
    var isFrontFace = false
    var isFlash = false


    fun createImageFile(pictureResult: PictureResult) {
        viewModelScope.launch {
            val rv = CreateImageFileAction.CreateRV(pictureResult)
            CreateImageFileAction().invoke(rv).catch {

            }.collect {

            }
        }
    }
}
