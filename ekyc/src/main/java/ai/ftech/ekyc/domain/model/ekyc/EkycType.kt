package ai.ftech.ekyc.domain.model.ekyc

enum class PHOTO_TYPE(var value: String){
    SSN_FRONT("FRONT"),
    SSN_BACK("BACK"),

    DRIVER_LICENSE_FRONT("FRONT"),
    DRIVER_LICENSE_BACK("BACK"),

    PASSPORT_FRONT(""),

    PORTRAIT("")
}
