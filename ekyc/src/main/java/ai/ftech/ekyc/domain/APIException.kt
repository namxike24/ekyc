package ai.ftech.ekyc.domain

class APIException : Exception {

    companion object {
        const val UNKNOWN_ERROR = "-20001"
        const val NETWORK_ERROR = "-20002"
        const val TIME_OUT_ERROR = "-20003"
        const val SERVER_ERROR_CODE_UNDEFINE = "-20004"
        const val RESPONSE_BODY_ERROR = "-20005"
        const val CREATE_INSTANCE_SERVICE_ERROR = "-20006"

        // server error code

    }

    var code = ""
        private set
    var payload: Any? = null
        private set

    constructor(code: String) : super() {
        this.code = code
    }

    constructor(code: String, message: String?) : super(message) {
        this.code = code
    }

    constructor(code: String, message: String?, payload: Any?) : super(message) {
        this.code = code
        this.payload = payload
    }

    constructor(t: Throwable?) : super(t)

    constructor(message: String?, t: Throwable?) : super(message, t)
}
