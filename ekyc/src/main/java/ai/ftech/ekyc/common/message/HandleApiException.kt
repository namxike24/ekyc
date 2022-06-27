package ai.ftech.ekyc.common.message

import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.getAppString
import ai.ftech.ekyc.domain.APIException
import android.content.Context

object HandleApiException : IAPIMessage {

    override fun getAPIMessage(context: Context?, exception: APIException): String? {
        return when (exception.code) {
            APIException.NETWORK_ERROR -> getAppString(R.string.fekyc_no_network)
            APIException.TIME_OUT_ERROR -> getAppString(R.string.fekyc_sever_time_out)

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
