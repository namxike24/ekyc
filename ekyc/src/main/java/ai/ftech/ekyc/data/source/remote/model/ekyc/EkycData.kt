package ai.ftech.ekyc.data.source.remote.model.ekyc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EkycData {
    @SerializedName("identityType")
    @Expose
    var identityType: String? = null

    @SerializedName("identityName")
    @Expose
    var identityName: String? = null

    @SerializedName("form")
    @Expose
    var formList: List<EkycFormData>? = null
}
