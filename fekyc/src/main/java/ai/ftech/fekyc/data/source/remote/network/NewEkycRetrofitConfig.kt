package ai.ftech.fekyc.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NewEkycRetrofitConfig(private val accessToken: String?) : BaseRetrofitConfig() {
    override fun getUrl() = ApiConfig.BASE_URL_NEW_FEKYC

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(ContentTypeJson(accessToken))
    }

    class ContentTypeJson(private val accessToken: String?) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = buildChain(chain)
            if (!accessToken.isNullOrBlank()) {
                requestBuilder.addHeader(ApiConfig.HeaderName.AUTHORIZATION, "Bearer $accessToken")
            }
            return chain.proceed(requestBuilder.build())
        }

        private fun buildChain(chain: Interceptor.Chain): Request.Builder {
            val request = chain.request()
            val builder = request.newBuilder()
            builder.addHeader(ApiConfig.HeaderName.CONTENT_TYPE, ApiConfig.HeaderValue.APPLICATION_JSON)
            builder.method(request.method, request.body)

            return builder
        }
    }

}
