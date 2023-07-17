package ai.ftech.fekyc.domain.model.facematching

class FaceMatchingData {
    var sessionId: String? = null
    var cardInfo: CardInfo? = null

    class CardInfo {
        var id: String? = null

        var birthDay: String? = null

        var birthPlace: String? = null

        var cardType: String? = null

        var gender: String? = null

        var issueDate: String? = null

        var issuePlace: String? = null

        var name: String? = null

        var nationality: String? = null

        var originLocation: String? = null

        var passportNo: String? = null

        var recentLocation: String? = null

        var validDate: String? = null

        var feature: String? = null

        var nation: String? = null

        var religion: String? = null

        var mrz: String? = null
    }
}