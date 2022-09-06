package ai.ftech.ekyc.domain

class APIException : Exception {
    companion object {
        const val UNKNOWN_ERROR = -2001
        const val NETWORK_ERROR = -2002
        const val TIME_OUT_ERROR = -2003
        const val SERVER_ERROR_CODE_UNDEFINE = -2004
        const val RESPONSE_BODY_ERROR = -2005
        const val CREATE_INSTANCE_SERVICE_ERROR = -2006
        const val EXPIRE_SESSION_ERROR = 401

        // server error code

    }

    var code = 0
        private set
    var payload: Any? = null
        private set

    constructor(message: String?) : super(message) {}

    constructor(t: Throwable?) : super(t) {}

    constructor(message: String?, t: Throwable?) : super(message, t) {}

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(code: Int, message: String?) : super(message) {
        this.code = code
    }

    constructor(code: Int, t: Throwable?) : super(t) {
        this.code = code
    }

    constructor(code: Int,message: String?, payload: Any?) : super(message) {
        this.code = code
        this.payload = payload
    }
}
