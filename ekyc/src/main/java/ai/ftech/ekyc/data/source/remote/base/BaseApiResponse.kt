package ai.ftech.ekyc.data.source.remote.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseApiResponse : IApiResponse {
    @SerializedName("status")
    @Expose
    var message: String? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: String? = null

    override fun isSuccessful(): Boolean {
        return message == "success" && statusCode == "OK"
    }
}
