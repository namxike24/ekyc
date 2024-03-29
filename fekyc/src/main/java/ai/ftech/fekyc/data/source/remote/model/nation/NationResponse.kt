package ai.ftech.fekyc.data.source.remote.model.nation

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NationResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: List<NationData>? = null
}
