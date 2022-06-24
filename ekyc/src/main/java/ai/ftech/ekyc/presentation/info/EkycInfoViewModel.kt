package ai.ftech.ekyc.presentation.info

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.dev.base.common.BaseViewModel
import ai.ftech.dev.base.extension.asLiveData
import ai.ftech.ekyc.common.action.FEkycActionResult
import ai.ftech.ekyc.domain.action.GetCityListAction
import ai.ftech.ekyc.domain.action.GetEkycInfoAction
import ai.ftech.ekyc.domain.action.GetNationListAction
import ai.ftech.ekyc.domain.action.SubmitInfoAction
import ai.ftech.ekyc.domain.model.address.City
import ai.ftech.ekyc.domain.model.address.Nation
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EkycInfoViewModel : BaseViewModel() {
    private val _ekycInfo = MutableLiveData<EkycInfo>()
    val ekycInfo = _ekycInfo.asLiveData()

    private val _submitInfo = MutableLiveData(FEkycActionResult<Boolean>())
    val submitInfo = _submitInfo.asLiveData()
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
                SubmitInfoAction().invoke(rv).catch {
                    _submitInfo.value?.exception = it
                    _submitInfo.value?.data = false
                }.collect {
                    _submitInfo.value?.data = it
                }
            }
        }
    }

    fun getEkycInfo() {
        viewModelScope.launch {
            GetEkycInfoAction().invoke(BaseAction.VoidRequest()).catch {

            }.collect {
                _ekycInfo.value = it
                ekycInfoLocal = it
            }
        }
    }

    fun getCityList() {
        viewModelScope.launch {
            GetCityListAction().invoke(BaseAction.VoidRequest()).catch {

            }.collect {
                cityList = it
            }
        }
    }

    fun getNationList() {
        viewModelScope.launch {
            GetNationListAction().invoke(BaseAction.VoidRequest()).catch {

            }.collect {
                nationList = it
            }
        }
    }
}
