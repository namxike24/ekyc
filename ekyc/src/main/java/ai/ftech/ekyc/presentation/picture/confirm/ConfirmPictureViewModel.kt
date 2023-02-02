package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.ekyc.base.common.BaseAction
import ai.ftech.ekyc.base.common.BaseViewModel
import ai.ftech.ekyc.common.onException
import ai.ftech.ekyc.domain.action.GetConfirmPhotoListAction
import ai.ftech.ekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConfirmPictureViewModel : BaseViewModel() {
    var photoConfirmDetailInfoList = MutableLiveData(mutableListOf<PhotoConfirmDetailInfo>())
        private set

    var photoInfoList = MutableLiveData<List<PhotoInfo>>()
        private set

    private var selectedPosition = -1

    fun getConfirmPhotoList() {
        viewModelScope.launch {
            GetConfirmPhotoListAction().invoke(BaseAction.VoidRequest()).onException {

            }.collect {
                photoConfirmDetailInfoList.value = it.toMutableList()
                photoInfoList.value = getPhotoInfoList(it)
            }
        }
    }

    fun setSelectedIndex(item: PhotoInfo) {
        selectedPosition = photoInfoList.value?.indexOf(item) ?: -1
    }

    fun setSelectedIndex(pos: Int) {
        selectedPosition = pos
    }

    fun getSelectedIndex(): Int {
        return selectedPosition
    }

    fun getItemSelected(): PhotoInfo? {
        return photoInfoList.value?.get(getSelectedIndex())
    }

    fun clearSelected() {
        selectedPosition = -1
    }

    private fun getPhotoInfoList(list: List<PhotoConfirmDetailInfo>): List<PhotoInfo> {
        val result = mutableListOf<PhotoInfo>()
        list.forEach {
            result.addAll(it.photoList)
        }
        return result
    }
}
