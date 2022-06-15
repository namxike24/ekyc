package ai.ftech.ekyc.data.source.remote.base

import ai.ftech.ekyc.domain.APIException
import retrofit2.Response

object ExceptionHelper {
    // INFORMATION

    // SUCCESSFUL
    private const val HTTP_CODE_SUCCESSFUL_OK = 200

    // REDIRECTION

    // CLIENT ERROR

    // SERVER ERROR

    fun throwException(response: Response<*>): APIException {
        return when (response.code()) {
            HTTP_CODE_SUCCESSFUL_OK -> getAPIExceptionWhenHTTPCodeSuccessful(response)
            else -> APIException(response.message(), response.code())
        }
    }

    private fun getAPIExceptionWhenHTTPCodeSuccessful(response: Response<*>): APIException {
        val body = response.body()
        if (body != null) {
            val apiResponse = body as BaseApiResponse
            val msg = apiResponse.message
            val status = apiResponse.statusCode ?: APIException.SERVER_ERROR_CODE_UNDEFINE
            return APIException(msg, status)
        }
        return APIException("body null", APIException.RESPONSE_BODY_ERROR)
    }
}
