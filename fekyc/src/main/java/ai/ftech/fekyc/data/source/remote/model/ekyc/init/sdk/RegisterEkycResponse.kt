package ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterEkycResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: RegisterEkycData? = null

    override fun isSuccessful(): Boolean {
        return data != null
    }
}
