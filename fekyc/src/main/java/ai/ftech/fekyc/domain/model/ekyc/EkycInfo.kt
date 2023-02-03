package ai.ftech.fekyc.domain.model.ekyc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EkycInfo {
    @Expose @SerializedName("identityType")
    var identityType: String? = null

    @Expose @SerializedName("identityName")
    var identityName: String? = null

    @Expose @SerializedName("form")
    var form: List<EkycFormInfo>? = null
}
