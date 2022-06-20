package ai.ftech.ekyc.data.source.remote.model.city

import ai.ftech.ekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: List<CityData>? = null
}
