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
    private val _photoConfirmDetailInfoList = MutableLiveData(mutableListOf<PhotoConfirmDetailInfo>())
    val photoConfirmDetailInfoList = _photoConfirmDetailInfoList.asLiveData()

    private val _photoInfoList = MutableLiveData<List<PhotoInfo>>()
    val photoInfoList = _photoInfoList.asLiveData()

    fun getConfirmPhotoList() {
        viewModelScope.launch {
            GetConfirmPhotoListAction().invoke(BaseAction.VoidRequest()).catch {

            }.collect {
                _photoConfirmDetailInfoList.value = it.toMutableList()
                _photoInfoList.value = getPhotoInfoList(it)
            }
        }
    }

    private fun getPhotoInfoList(list: List<PhotoConfirmDetailInfo>): List<PhotoInfo> {
        val result = mutableListOf<PhotoInfo>()
        list.forEach {
            result.addAll(it.photoList)
        }
        return result
    }
}
