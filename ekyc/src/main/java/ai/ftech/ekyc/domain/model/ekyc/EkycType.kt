package ai.ftech.ekyc.domain.model.ekyc

enum class PHOTO_TYPE{
    SSN,
    DRIVER_LICENSE,
    PASSPORT,
    PORTRAIT
}

enum class UPLOAD_PHOTO_TYPE(val type: String) {
    PASSPORT(""),
    FRONT("FRONT"),
    BACK("BACK"),
    FACE(""),
    DONE("")
}
