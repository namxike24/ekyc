package ai.ftech.ekyc.domain.model.ekyc

class PhotoConfirmDetailInfo {
    var photoType: PHOTO_TYPE? = null
    var photoList: MutableList<PhotoInfo> = mutableListOf()

    enum class PHOTO_TYPE{
        SSN,
        DRIVER_LICENSE,
        PASSPORT,
        PORTRAIT
    }
}
