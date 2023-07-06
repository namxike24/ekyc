package ai.ftech.fekyc.data.source.remote.model.ekyc.submit

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewSubmitInfoResponse : BaseApiResponse() {
    @SerializedName("session_id")
    @Expose
    var sessionId: String? = null
}