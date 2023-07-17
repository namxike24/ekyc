package ai.ftech.fekyc.data.source.remote.model.ekyc.capture

import ai.ftech.fekyc.data.source.remote.base.IApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CaptureResponse : IApiResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("msg")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: DataCapture? = null

    class DataCapture {
        @SerializedName("session_id")
        @Expose
        var sessionId: String? = null
    }

    override fun isSuccessful(): Boolean {
        return message == "success"
    }
}


