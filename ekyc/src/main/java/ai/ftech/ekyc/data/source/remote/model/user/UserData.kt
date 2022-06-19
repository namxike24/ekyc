package ai.ftech.ekyc.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserData {
    @SerializedName("identityType")
    @Expose
    var identityType: String? = null

    @SerializedName("identityName")
    @Expose
    var identityName: String? = null

    @SerializedName("form")
    @Expose
    var formList: List<FormData>? = null
}
