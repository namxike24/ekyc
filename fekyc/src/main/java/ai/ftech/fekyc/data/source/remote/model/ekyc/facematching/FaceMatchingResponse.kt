package ai.ftech.fekyc.data.source.remote.model.ekyc.facematching

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FaceMatchingResponse : BaseApiResponse() {
    @SerializedName("data")
    @Expose
    val data: Data? = null

    class Data {
        @SerializedName("session_id")
        @Expose
        val sessionId: String? = null

        @SerializedName("card_info")
        @Expose
        val cardInfo: CardInfo? = null

        class CardInfo {
            @SerializedName("id")
            @Expose
            val id: String? = null

            @SerializedName("birth_day")
            @Expose
            val birthDay: String? = null

            @SerializedName("birth_place")
            @Expose
            val birthPlace: String? = null

            @SerializedName("card_type")
            @Expose
            val cardType: String? = null

            @SerializedName("gender")
            @Expose
            val gender: String? = null

            @SerializedName("issue_date")
            @Expose
            val issueDate: String? = null

            @SerializedName("issue_place")
            @Expose
            val issuePlace: String? = null

            @SerializedName("name")
            @Expose
            val name: String? = null

            @SerializedName("nationality")
            @Expose
            val nationality: String? = null

            @SerializedName("origin_location")
            @Expose
            val originLocation: String? = null

            @SerializedName("passport_no")
            @Expose
            val passportNo: String? = null

            @SerializedName("recent_location")
            @Expose
            val recentLocation: String? = null

            @SerializedName("valid_date")
            @Expose
            val validDate: String? = null

            @SerializedName("feature")
            @Expose
            val feature: String? = null

            @SerializedName("nation")
            @Expose
            val nation: String? = null

            @SerializedName("religion")
            @Expose
            val religion: String? = null

            @SerializedName("mrz")
            @Expose
            val mrz: String? = null
        }
    }
}