package ai.ftech.ekyc.publish

import ai.ftech.ekyc.domain.APIException

private object FTechCode {
    const val EKYC_SUCCESSFULLY = 0
    const val EXPIRE_SESSION_ERROR = APIException.EXPIRE_SESSION_ERROR
}

private object FTechMessage {
    const val EKYC_SUCCESSFULLY = "Ekyc thành công!"
    const val EXPIRE_SESSION_ERROR = "Phiên làm việc hết hạn"
}

internal enum class FTECH_STATE(val code: Int, val message: String) {

    EKYC_SUCCESSFULLY(FTechCode.EKYC_SUCCESSFULLY, FTechMessage.EKYC_SUCCESSFULLY),

    EXPIRE_SESSION_ERROR(FTechCode.EXPIRE_SESSION_ERROR, FTechMessage.EXPIRE_SESSION_ERROR)
}
