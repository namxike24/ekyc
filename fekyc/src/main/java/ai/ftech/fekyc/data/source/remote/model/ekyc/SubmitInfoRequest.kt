package ai.ftech.fekyc.data.source.remote.model.ekyc

import ai.ftech.fekyc.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubmitInfoRequest : BaseApiRequest() {
    @SerializedName("data")
    @Expose
    var data: String? = null
}
