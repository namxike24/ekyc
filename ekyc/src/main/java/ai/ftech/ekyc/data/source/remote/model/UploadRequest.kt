package ai.ftech.ekyc.data.source.remote.model

import ai.ftech.ekyc.data.source.remote.base.BaseApiRequest

class UploadRequest : BaseApiRequest() {
    var type: String? = null
}
