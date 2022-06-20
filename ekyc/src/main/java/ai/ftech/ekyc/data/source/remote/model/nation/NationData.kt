package ai.ftech.ekyc.data.source.remote.model.nation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NationData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}
