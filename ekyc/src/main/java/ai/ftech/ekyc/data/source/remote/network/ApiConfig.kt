package ai.ftech.ekyc.data.source.remote.network

object ApiConfig {
    const val BASE_URL_FEKYC = "https://api-ai-service.dev.ftech.ai"

    object HeaderName {
        const val CONTENT_TYPE = "Content-Type"
    }

    object HeaderValue {
        const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON = "application/json"
    }
}
