package ai.ftech.fekyc.domain.model.ekyc

import android.util.Log

class EkycFormInfo {
    var id: Int? = null
    var cardAttributeId: Int? = null
    var fieldName: String? = null
    var fieldValue: String? = null
    var type: String? = null
    var isEditable: Boolean = false
    var placeholder: String? = null
    var fieldType: FIELD_TYPE? = null
    var dateType: DATE_TYPE? = null

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

    enum class DATE_TYPE(val type: Int?) {
        NORMAL(0),
        PASS(1),
        FEATURE(2);

        companion object {
            fun valueOfName(value: Int?): DATE_TYPE? {
                val item = values().find {
                    it.type == value
                }

                return if (item != null) {
                    item
                } else {
                    null
                }
            }
        }
    }

    enum class GENDER(val value: String) {
        UNKNOWN("UNKNOWN"),
        MALE("Nam"),
        FEMALE("Nữ");

        companion object {
            fun valueOfName(value: String): GENDER? {
                val item = values().find {
                    it.value == value
                }

                return if (item != null) {
                    item
                } else {
                    Log.e("GENDER", "can not find any GENDER for name: $value")
                    null
                }
            }
        }
    }
}
