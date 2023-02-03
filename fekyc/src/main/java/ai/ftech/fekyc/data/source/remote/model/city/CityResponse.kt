package ai.ftech.fekyc.data.source.remote.model.city

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: List<CityData>? = null
}
