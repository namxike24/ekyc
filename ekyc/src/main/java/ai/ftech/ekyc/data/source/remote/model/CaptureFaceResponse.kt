package ai.ftech.ekyc.data.source.remote.model

import ai.ftech.ekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CaptureFaceResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: CaptureFaceData? = null
}
