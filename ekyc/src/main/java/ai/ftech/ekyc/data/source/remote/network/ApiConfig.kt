package ai.ftech.ekyc.data.source.remote.network

import ai.ftech.ekyc.data.source.remote.base.IApiService

object ApiConfig {
    const val BASE_URL_FEKYC = "https://api-ai-service.dev.ftech.ai"

    object HeaderName {
        const val CONTENT_TYPE = "Content-Type"
        const val FTECH_KEY = "ftechKey"
        const val APP_ID = "appID"
        const val TRANSACTION_ID = "transactionId"
        const val LANGUAGE = "language"
    }

    object HeaderValue {
        const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON = "application/json"
    }

    enum class API_LANGUAGE(val language: String) {
        VI("vi")
    }
}

fun <T : IApiService> invokeFEkycService(service: Class<T>): T {
    return RetrofitFactory.createFEkycService(service)
}
