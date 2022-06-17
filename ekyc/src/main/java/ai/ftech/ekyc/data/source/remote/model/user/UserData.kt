package ai.ftech.ekyc.data.source.remote.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserData {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("fieldName")
    @Expose
    var fieldName: String? = null

    @SerializedName("fieldValue")
    @Expose
    var fieldValue: String? = null

    @SerializedName("fieldType")
    @Expose
    var fieldType: String? = null

    @SerializedName("editable")
    @Expose
    var editable: Boolean? = null
}
