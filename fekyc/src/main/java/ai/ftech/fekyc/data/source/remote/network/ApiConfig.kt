package ai.ftech.fekyc.data.source.remote.network

object ApiConfig {
    const val BASE_URL_FEKYC = "https://api-ai-service.dev.ftech.ai"
    const val BASE_URL_INIT_FEKYC = "https://fcloud-api-gateway.dev.ftech.ai"
    const val BASE_URL_NEW_FEKYC = "https://fcloud-ekyc-api.dev.ftech.ai"

    object HeaderName {
        const val CONTENT_TYPE = "Content-Type"
        const val FTECH_KEY = "ftechKey"
        const val APP_ID = "appID"
        const val TRANSACTION_ID = "transactionId"
        const val LANGUAGE = "language"
        const val AUTHORIZATION = "Authorization"
    }

    object HeaderValue {
        const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON = "application/json"
    }

    enum class API_LANGUAGE(val language: String) {
        VI("vi")
    }
}
