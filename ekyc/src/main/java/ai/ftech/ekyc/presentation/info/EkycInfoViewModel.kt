package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.postSelf
import ai.ftech.ekyc.common.action.FEkycActionResult
import ai.ftech.ekyc.common.onException
import ai.ftech.ekyc.domain.action.GetCityListAction
import ai.ftech.ekyc.domain.action.GetEkycInfoAction
import ai.ftech.ekyc.domain.action.GetNationListAction
import ai.ftech.ekyc.domain.action.SubmitInfoAction
import ai.ftech.ekyc.domain.model.address.City
import ai.ftech.ekyc.domain.model.address.Nation
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EkycInfoViewModel : BaseViewModel() {
    var ekycInfo = MutableLiveData<EkycInfo>()
        private set

    var submitInfo = MutableLiveData(FEkycActionResult<Boolean>())
        private set

    var ekycInfoLocal: EkycInfo? = null

    var cityList: List<City> = emptyList()
        private set

    var nationList: List<Nation> = emptyList()
        private set

    fun submitInfo(list: MutableList<EkycFormInfo>) {
        viewModelScope.launch {
            ekycInfoLocal?.formList = list
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

            }.collect {
                ekycInfo.value = it
                ekycInfoLocal = it
            }
        }
    }

    fun getCityList() {
        viewModelScope.launch {
            GetCityListAction().invoke(BaseAction.VoidRequest()).onException {

            }.collect {
                cityList = it
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
