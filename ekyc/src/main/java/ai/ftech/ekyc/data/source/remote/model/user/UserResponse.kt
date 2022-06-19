package ai.ftech.ekyc.data.source.remote.model.user

import ai.ftech.ekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    var data: UserData? = null
}
