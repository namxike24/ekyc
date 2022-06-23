package ai.ftech.ekyc.common.action

class FEkycActionResult<DATA> {
    var data: DATA? = null
        set(value) {
            resultStatus = RESULT_STATUS.SUCCESS
            field = value
        }

    var exception: Throwable? = null
        set(value) {
            resultStatus = RESULT_STATUS.ERROR
            field = value
        }

    var resultStatus: RESULT_STATUS = RESULT_STATUS.UNKNOWN

    enum class RESULT_STATUS {
        SUCCESS,
        ERROR,
        UNKNOWN
    }
}
