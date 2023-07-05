package ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InitSDKData {
    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("ts")
    @Expose
    var ts: Long? = null

    @SerializedName("service")
    @Expose
    var serviceData: ServiceData? = null
}
