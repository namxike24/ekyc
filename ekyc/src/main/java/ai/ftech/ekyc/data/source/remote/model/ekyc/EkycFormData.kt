package ai.ftech.ekyc.data.source.remote.model.ekyc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EkycFormData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("fieldName")
    @Expose
    var fieldName: String? = null

    @SerializedName("fieldValue")
    @Expose
    var fieldValue: String? = null

    @SerializedName("cardAttributeId")
    @Expose
    var cardAttributeId: Int? = null

    @SerializedName("fieldType")
    @Expose
    var fieldType: String? = null

    @SerializedName("editable")
    @Expose
    var editable: Boolean? = null

    @SerializedName("dateType")
    @Expose
    var dateType: Int? = null
}
