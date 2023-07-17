package ai.ftech.fekyc.data.source.remote.model.ekyc.facematching

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FaceMatchingRequest {
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String? = null

    @SerializedName("session_id_front")
    @Expose
    var sessionIdFront: String? = null

    @SerializedName("session_id_back")
    @Expose
    var sessionIdBack: String? = null

    @SerializedName("session_id_face")
    @Expose
    var sessionIdFace: String? = null
}