package ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("info")
    @Expose
    var info: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null
}
