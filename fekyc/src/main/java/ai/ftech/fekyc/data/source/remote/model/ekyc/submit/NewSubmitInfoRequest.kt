package ai.ftech.fekyc.data.source.remote.model.ekyc.submit

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class NewSubmitInfoRequest {
    @SerializedName("card_info_submit")
    @Expose
    var cardInfoSubmit: CardInfoSubmit? = null

    @SerializedName("pre_process_id")
    @Expose
    var preProcessId: String? = null
    class CardInfoSubmit {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("birth_day")
        @Expose
        var birthDay: String? = null

        @SerializedName("birth_place")
        @Expose
        var birthPlace: String? = null

        @SerializedName("card_type")
        @Expose
        var cardType: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("issue_date")
        @Expose
        var issueDate: String? = null

        @SerializedName("issue_place")
        @Expose
        var issuePlace: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("nationality")
        @Expose
        var nationality: String? = null

        @SerializedName("origin_location")
        @Expose
        var originLocation: String? = null

        @SerializedName("passport_no")
        @Expose
        var passportNo: String? = null

        @SerializedName("recent_location")
        @Expose
        var recentLocation: String? = null

        @SerializedName("valid_date")
        @Expose
        var validDate: String? = null

        @SerializedName("feature")
        @Expose
        var feature: String? = null

        @SerializedName("nation")
        @Expose
        var nation: String? = null

        @SerializedName("religion")
        @Expose
        var religion: String? = null

        @SerializedName("mrz")
        @Expose
        var mrz: String? = null

        override fun toString(): String {
            return "id: $id, name: $name"
        }
    }

    override fun toString(): String {
        return "pre_process_id: $preProcessId, card_info_submit: ${cardInfoSubmit.toString()}"
    }
}