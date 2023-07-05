package ai.ftech.fekyc.data.source.remote.model.ekyc.transaction

import ai.ftech.fekyc.data.source.remote.base.BaseApiRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    @SerializedName("extra_data")
    @Expose
    var extraData: String? = null
) : BaseApiRequest()

