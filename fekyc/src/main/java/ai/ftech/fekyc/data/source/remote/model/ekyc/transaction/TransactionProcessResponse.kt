package ai.ftech.fekyc.data.source.remote.model.ekyc.transaction

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionProcessResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    val data: TransactionProcessDataResponse? = null

    class TransactionProcessDataResponse {
        @SerializedName("session_id_front")
        @Expose
        val sessionIdFront: String? = null

        @SerializedName("session_id_back")
        @Expose
        val sessionIdBack: String? = null

        @SerializedName("session_id_face")
        @Expose
        val sessionIdFace: String? = null

        @SerializedName("process_id")
        @Expose
        val processId: String? = null

        @SerializedName("submit_id")
        @Expose
        val submitId: String? = null
    }
}