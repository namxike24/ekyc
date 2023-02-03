package ai.ftech.fekyc.data.source.remote.model.ekyc

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EkycResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: EkycData? = null
}
