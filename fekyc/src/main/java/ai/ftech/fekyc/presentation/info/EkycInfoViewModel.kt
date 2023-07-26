package ai.ftech.fekyc.presentation.info

import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.common.BaseViewModel
import ai.ftech.fekyc.base.extension.postSelf
import ai.ftech.fekyc.common.action.FEkycActionResult
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.data.repo.converter.FaceMatchingDataConvertToSubmitRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.domain.model.address.City
import ai.ftech.fekyc.domain.model.address.Nation
import ai.ftech.fekyc.domain.model.ekyc.EkycFormInfo
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData
import ai.ftech.fekyc.publish.FTechEkycManager
import ai.ftech.fekyc.publish.IFTechEkycCallback
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.json.JSONObject

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

    private var currentSessionId = ""

    fun submitInfo(list: List<EkycFormInfo>) {
        if (currentSessionId.isEmpty()) return
        val newSubmitData: NewSubmitInfoRequest = getNewSubmitInfo(list)
        FTechEkycManager.submitInfo(newSubmitData, object : IFTechEkycCallback<Boolean> {
            override fun onSuccess(info: Boolean) {
                super.onSuccess(info)
                submitInfo.value?.data = info
                submitInfo.postSelf()
            }

            override fun onFail(error: APIException?) {
                super.onFail(error)
                submitInfo.value?.exception = error
                submitInfo.postSelf()
            }
        })
    }

    private fun getNewSubmitInfo(list: List<EkycFormInfo>): NewSubmitInfoRequest {
        val infoJsonObject = JSONObject()
        list.forEach { info ->
            infoJsonObject.put(info.fieldName.orEmpty(), info.fieldValue)
        }
        val cardInfoData = Gson().fromJson(infoJsonObject.toString(),FaceMatchingData.CardInfo::class.java)
        val faceMatchingData = FaceMatchingData().apply {
            this.sessionId = currentSessionId
            this.cardInfo = cardInfoData
        }
        return FaceMatchingDataConvertToSubmitRequest().convert(faceMatchingData)
    }

    fun getFaceMatchingData() {
        FTechEkycManager.faceMatching(object : IFTechEkycCallback<FaceMatchingData> {
            override fun onSuccess(info: FaceMatchingData) {
                super.onSuccess(info)
                currentSessionId = info.sessionId.orEmpty()
                ekycInfo.value?.data = convertMatchingDataToEkycInfo(info.cardInfo)
                ekycInfo.postSelf()
            }

            override fun onFail(error: APIException?) {
                super.onFail(error)
                ekycInfo.value?.exception = error
                ekycInfo.postSelf()
            }
        })
    }

    private fun convertMatchingDataToEkycInfo(info: FaceMatchingData.CardInfo?): EkycInfo {
        if (info == null)
            return EkycInfo()
        return EkycInfo().apply {
            this.identityType = getAppString(R.string.mock_title_card_type)
            this.identityName = info.cardType
            this.form = getFormInfo(info)
        }
    }

    private fun getFormInfo(info: FaceMatchingData.CardInfo): List<EkycFormInfo> {
        val listInfo = arrayListOf<EkycFormInfo>()

        val infoJson = Gson().toJson(info)
        val infoJsonObject = JSONObject(infoJson)
        val fieldInfo = infoJsonObject.keys()
        fieldInfo.forEach {
            val value = infoJsonObject[it]
            listInfo.add(EkycFormInfo().apply {
                this.fieldName = it
                this.fieldValue = value.toString()
                this.fieldType = EkycFormInfo.FIELD_TYPE.STRING
                this.isEditable = true
            })
        }
        return listInfo
    }

}
