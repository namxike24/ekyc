package ai.ftech.ekyc.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class EkycRetrofitConfig(
    val ftechKey: String,
    val appID: String,
    val transactionId: String,
    val language: String,
) : BaseRetrofitConfig() {
    override fun getUrl() = ApiConfig.BASE_URL_FEKYC

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(ContentTypeJson(ftechKey, appID, transactionId, language))
    }

    class ContentTypeJson(
        val ftechKey: String,
        val appID: String,
        val transactionId: String,
        val language: String,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = buildChain(chain)
            return chain.proceed(requestBuilder.build())
        }

        private fun buildChain(chain: Interceptor.Chain): Request.Builder {
            val original = chain.request()
            val builder = original.newBuilder()
            builder.addHeader(ApiConfig.HeaderName.CONTENT_TYPE, ApiConfig.HeaderValue.APPLICATION_JSON)

            if (ftechKey.isNotBlank()) {
                builder.addHeader(ApiConfig.HeaderName.FTECH_KEY, ftechKey)
            }

            if (appID.isNotBlank()) {
                builder.addHeader(ApiConfig.HeaderName.APP_ID, appID)
            }

            if (transactionId.isNotBlank()) {
                builder.addHeader(ApiConfig.HeaderName.TRANSACTION_ID, transactionId)
            }

            if (language.isNotBlank()) {
                builder.addHeader(ApiConfig.HeaderName.LANGUAGE, language)
            }

            builder.method(original.method, original.body)
            return builder
        }
    }
}
