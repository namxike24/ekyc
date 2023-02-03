package ai.ftech.fekyc.common.message

import ai.ftech.fekyc.domain.APIException
import android.content.Context

interface IAPIMessage {
    fun getAPIMessage(context: Context?, exception: APIException): String?
}
