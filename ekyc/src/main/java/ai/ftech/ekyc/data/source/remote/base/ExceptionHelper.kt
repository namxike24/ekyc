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
            else -> APIException(response.code(), response.message())
        }
    }

    private fun getAPIExceptionWhenHTTPCodeSuccessful(response: Response<*>): APIException {
        val body = response.body()
        if (body != null) {
            val apiResponse = body as BaseApiResponse
            val status = apiResponse.status
            val code = apiResponse.statusCode ?: APIException.SERVER_ERROR_CODE_UNDEFINE
            val msg = apiResponse.message
            return APIException(code, msg)
        }
        return APIException(APIException.RESPONSE_BODY_ERROR, "body null")
    }
}
