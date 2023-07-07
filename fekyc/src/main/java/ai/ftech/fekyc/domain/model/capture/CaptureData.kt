package ai.ftech.fekyc.domain.model.capture

class CaptureData {
    var code: Int? = null
    var message: String? = null
    var data: SessionData? = null

    class SessionData {
        var sessionId: String? = null
    }
}