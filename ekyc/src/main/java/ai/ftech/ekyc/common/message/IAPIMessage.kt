package ai.ftech.ekyc.common.message

import ai.ftech.ekyc.domain.APIException
import android.content.Context

interface IAPIMessage {
    fun getAPIMessage(context: Context?, exception: APIException): String?
}
