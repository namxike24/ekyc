package ai.ftech.fekyc.data.source.remote.base

import ai.ftech.fekyc.domain.APIException
import com.google.gson.Gson
import retrofit2.Response

object ExceptionHelper {
    private const val HTTP_CODE_SUCCESSFUL_OK = 200

    fun throwException(response: Response<*>): APIException {
        return when (response.code()) {
            HTTP_CODE_SUCCESSFUL_OK -> getAPIExceptionWhenHTTPCodeSuccessful(response)
            else -> getAPIExceptionWhenHTTPCodeUnsuccessful(response)
        }
    }

    private fun getAPIExceptionWhenHTTPCodeSuccessful(response: Response<*>): APIException {
        val body = response.body()
        if (body != null) {
            val apiResponse = body as BaseApiResponse
//            val status = apiResponse.status
            val code = apiResponse.code ?: APIException.SERVER_ERROR_CODE_UNDEFINE
            val msg = apiResponse.message
            return APIException(code, msg)
        }
        return APIException(APIException.RESPONSE_BODY_ERROR, "body null")
    }

    private fun getAPIExceptionWhenHTTPCodeUnsuccessful(response: Response<*>): APIException {
        val errorBody = response.errorBody()
        if (errorBody != null) {
            val errorResponse = Gson().fromJson(errorBody.charStream(), BaseApiResponse::class.java)
            return APIException(errorResponse?.code ?: APIException.UNKNOWN_ERROR, errorResponse?.message)
        }
        return APIException(response.code(), response.message())
    }
}
