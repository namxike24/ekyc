package ai.ftech.fekyc.common.message

import ai.ftech.fekyc.R
import ai.ftech.fekyc.common.getAppString
import ai.ftech.fekyc.domain.APIException
import android.content.Context

object HandleApiException : IAPIMessage {

    override fun getAPIMessage(context: Context?, exception: APIException): String {
        return when (exception.code) {
            APIException.NETWORK_ERROR -> getAppString(R.string.fekyc_no_network)
            APIException.TIME_OUT_ERROR -> getAppString(R.string.fekyc_sever_time_out)
            APIException.EXPIRE_SESSION_ERROR -> getAppString(R.string.fekyc_session_expire)
            else -> {
                if (exception.message.isNullOrBlank()) {
                    getAppString(R.string.fekyc_unknown_error)
                } else {
                    exception.message
                }
            }
        }
    }
}
