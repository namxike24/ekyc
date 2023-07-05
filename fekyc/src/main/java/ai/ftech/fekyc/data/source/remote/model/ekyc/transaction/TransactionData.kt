package ai.ftech.fekyc.data.source.remote.model.ekyc.transaction

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionData {
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String? = null
}
