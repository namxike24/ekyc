package ai.ftech.ekyc.domain.model.ekyc


class FormInfo {
    var id: Int? = null
    var title: String? = null
    var value: String? = null
    var type: String? = null
    var isEditable: Boolean = false
    var fieldActionType: FIELD_TYPE? = null

    enum class FIELD_TYPE {
        EDIT,
        TIME_PICKER,
        SELECT
    }
}


//class UserInfo {
//    var fullName: String = ""
//    var numberPapers: String = ""
//    var dateOfIssue: DateInfo? = null
//    var issuedBy: City? = null
//    var gender: GENDER = GENDER.UNKNOWN
//    var isEditable: Boolean = false
//    var nation: Nation? = null
//    var address: String = ""
//    var note: String = ""
//
//    enum class GENDER {
//        UNKNOWN,
//        MALE,
//        FEMALE
//    }
//}
