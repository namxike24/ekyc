package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.asLiveData
import ai.ftech.ekyc.domain.action.GetConfirmPhotoListAction
import ai.ftech.ekyc.domain.model.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.PhotoInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ConfirmPictureViewModel : BaseViewModel() {
    private val _photoList = MutableLiveData(mutableListOf<PhotoConfirmDetailInfo>())
    val photoList = _photoList.asLiveData()

    fun getConfirmPhotoList() {
        viewModelScope.launch {
            GetConfirmPhotoListAction().invoke(BaseAction.VoidRequest()).catch {

            }.collect {
                _photoList.value = it.toMutableList()
            }
        }
    }
}
