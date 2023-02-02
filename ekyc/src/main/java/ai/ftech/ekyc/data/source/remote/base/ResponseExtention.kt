package ai.ftech.ekyc.data.source.remote.base

import ai.ftech.ekyc.base.repo.BaseRepo
import ai.ftech.ekyc.data.source.remote.network.RetrofitFactory
import ai.ftech.ekyc.domain.APIException
import okhttp3.Headers
import retrofit2.Call
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <RESPONSE : IApiResponse, RETURN_VALUE> Call<RESPONSE>.invokeApi(block: (Headers, RESPONSE) -> RETURN_VALUE): RETURN_VALUE {
    try {
        val response = this.execute()
        if (response.isSuccessful) {
            val body: RESPONSE? = response.body()
            if (body != null) {
                if (body.isSuccessful()) {
                    return block(response.headers(), body)
                } else {
                    throw ExceptionHelper.throwException(response)
                }
            } else {
                throw ExceptionHelper.throwException(response)
            }
        } else {
            throw ExceptionHelper.throwException(response)
        }
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> {
                throw APIException(APIException.NETWORK_ERROR)
            }
            is SocketTimeoutException -> {
                throw APIException(APIException.TIME_OUT_ERROR)
            }
            else -> throw e
        }
    }
}

fun <T : IApiService> BaseRepo.invokeFEkycService(service: Class<T>): T {
    return RetrofitFactory.createFEkycService(service)
}
