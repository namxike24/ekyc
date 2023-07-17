package ai.ftech.fekyc.data.source.remote.model.ekyc.transaction

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionResponse: BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: TransactionData? = null
}
