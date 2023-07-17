package ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk

import ai.ftech.fekyc.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InitSDKRequest(
    @SerializedName("app_id")
    @Expose
    var appId: String? = null,

    @SerializedName("secret_key")
    @Expose
    var secretKey: String? = null
) : BaseApiRequest()

