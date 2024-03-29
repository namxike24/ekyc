package ai.ftech.fekyc.domain.model.ekyc

enum class PHOTO_TYPE {
    SSN,
    DRIVER_LICENSE,
    PASSPORT
}

enum class PHOTO_INFORMATION(var value: String) {
    FRONT("FRONT"),
    BACK("BACK"),
    FACE(""),
    PAGE_NUMBER_2("")
}

enum class UPLOAD_STATUS {
    NONE,
    SUCCESS,
    COMPLETE,
    FAIL
}

enum class CAPTURE_TYPE(var value: String?) {
    FRONT("front"),
    BACK("back"),
    FACE(null),
}


