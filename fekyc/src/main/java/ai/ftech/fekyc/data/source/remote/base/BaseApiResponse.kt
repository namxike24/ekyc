package ai.ftech.fekyc.data.source.remote.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseApiResponse : IApiResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    override fun isSuccessful(): Boolean {
        return status == "success" && statusCode == 0
    }
}
