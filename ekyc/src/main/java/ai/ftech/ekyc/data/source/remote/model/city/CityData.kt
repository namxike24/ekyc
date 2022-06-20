package ai.ftech.ekyc.data.source.remote.model.city

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("displayName")
    @Expose
    var displayName: String? = null
}
