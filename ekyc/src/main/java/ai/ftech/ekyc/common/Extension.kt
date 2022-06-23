package ai.ftech.ekyc.common

import ai.ftech.ekyc.FTechEkycManager
import ai.ftech.ekyc.common.message.HandleApiException
import ai.ftech.ekyc.domain.APIException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.onException(onCatch: (Throwable) -> Unit): Flow<T> {
    return catch { e ->
        if (e is APIException) {
            val msg = HandleApiException.getAPIMessage(FTechEkycManager.getApplicationContext(), e)
            val apiException = APIException(e.code, msg)
            onCatch(apiException)
        } else {
            onCatch(e)
        }
    }
}
