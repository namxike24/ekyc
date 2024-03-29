package ai.ftech.fekyc.data.source.remote.model.ekyc

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
    var form: List<EkycFormData>? = null
}
