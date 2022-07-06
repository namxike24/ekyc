package ai.ftech.ekyc.data.source.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CaptureFaceData {
    @SerializedName("action")
    @Expose
    var action: String? = null

    @SerializedName("request_id")
    @Expose
    var requestId: String? = null

    @SerializedName("transactionId")
    @Expose
    var transactionId: String? = null

    @SerializedName("isMatch")
    @Expose
    var isMatch: Boolean? = null

    @SerializedName("matchPercent")
    @Expose
    var matchPercent: Double? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
}
