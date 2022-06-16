package ai.ftech.ekyc.common

import ai.ftech.ekyc.FTechEkycManager
import ai.ftech.ekyc.common.message.HandleApiException
import ai.ftech.ekyc.domain.APIException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.onException(onCatch: (Throwable) -> Unit): Flow<T> {
    return catch { e ->
        if (e is APIException) {
            val apiException = APIException(HandleApiException.getAPIMessage(FTechEkycManager.getApplicationContext(), e), e.code)
            onCatch(apiException)
        } else {
            onCatch(e)
        }
    }
}
