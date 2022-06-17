package ai.ftech.ekyc.domain.model


class UserInfo {
    var id: Int = -1
    var title: String = ""
    var value: String = ""
    var isEditable: Boolean = false
    var fieldType: FIELD_TYPE? = null

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
