package ai.ftech.fekyc.presentation.info

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.base.common.BaseViewModel
import ai.ftech.fekyc.base.extension.postSelf
import ai.ftech.fekyc.common.action.FEkycActionResult
import ai.ftech.fekyc.common.onException
import ai.ftech.fekyc.domain.action.GetEkycInfoAction
import ai.ftech.fekyc.domain.action.GetNationListAction
import ai.ftech.fekyc.domain.action.SubmitInfoAction
import ai.ftech.fekyc.domain.model.address.City
import ai.ftech.fekyc.domain.model.address.Nation
import ai.ftech.fekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EkycInfoViewModel : BaseViewModel() {
    var ekycInfo = MutableLiveData(FEkycActionResult<EkycInfo>())
        private set

    var submitInfo = MutableLiveData(FEkycActionResult<Boolean>())
        private set

    var ekycInfoLocal: EkycInfo? = null

    var cityList: List<City> = emptyList()
        private set

    var nationList: List<Nation> = emptyList()
        private set

    fun submitInfo(list: List<EkycFormInfo>) {
        viewModelScope.launch {
            ekycInfoLocal?.form = list
            val data = ekycInfoLocal
            if (data != null) {
                val rv = SubmitInfoAction.SubmitRV(data)
                SubmitInfoAction().invoke(rv).onException {
                    submitInfo.value?.exception = it
                    submitInfo.postSelf()
                }.collect {
                    submitInfo.value?.data = it
                    submitInfo.postSelf()
                }
            }
        }
    }

    fun getEkycInfo() {
        viewModelScope.launch {
            GetEkycInfoAction().invoke(BaseAction.VoidRequest()).onException {
                ekycInfo.value?.exception = it
                ekycInfo.postSelf()
            }.collect {
                ekycInfoLocal = it
                ekycInfo.value?.data = it
                ekycInfo.postSelf()
            }

        }
    }

    fun getNationList() {
        viewModelScope.launch {
            GetNationListAction().invoke(BaseAction.VoidRequest()).onException {

            }.collect {
                nationList = it
            }
        }
    }
}