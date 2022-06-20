package ai.ftech.ekyc.domain.model.ekyc

import android.util.Log


class FormInfo {
    var id: Int? = null
    var title: String? = null
    var value: String? = null
    var type: String? = null
    var isEditable: Boolean = false
    var fieldType: FIELD_TYPE? = null

    enum class FIELD_TYPE(val type: String) {
        STRING("string"),
        NUMBER("number"),
        DATE("date"),
        COUNTRY("country"),
        GENDER("gender"),
        NATIONAL("national");

        companion object {
            fun valueOfName(value: String): FIELD_TYPE? {
                val item = values().find {
                    it.type == value
                }

                return if (item != null) {
                    item
                } else {
                    Log.e("FIELD_TYPE", "can not find any FIELD_TYPE for name: $value")
                    null
                }
            }
        }
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
