package ai.ftech.fekyc.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyIdentityData {
    @SerializedName("action")
    @Expose
    var action: String? = null

    @SerializedName("request_id")
    @Expose
    var requestId: String? = null

    @SerializedName("transactionId")
    @Expose
    var transactionId: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("fullName")
    @Expose
    var fullName: String? = null

    @SerializedName("identityNumber")
    @Expose
    var identityNumber: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
}
