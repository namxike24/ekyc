package ai.ftech.fekyc.data.source.remote.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseApiResponse : IApiResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("msg")
    @Expose
    var message: String? = null
//
    override fun isSuccessful(): Boolean {
        return code == 0
    }
}
