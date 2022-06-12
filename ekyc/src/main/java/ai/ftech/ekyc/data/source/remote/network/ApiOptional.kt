package ai.ftech.ekyc.data.source.remote.network

import ai.ftech.ekyc.data.source.remote.base.IApiService
import ai.ftech.ekyc.domain.APIException

fun <T : IApiService> invokeFEkycService(service: Class<T>): T {
    return RetrofitFactory.createFEkycService(service)
        ?: throw APIException(APIException.CREATE_INSTANCE_SERVICE_ERROR)
}
