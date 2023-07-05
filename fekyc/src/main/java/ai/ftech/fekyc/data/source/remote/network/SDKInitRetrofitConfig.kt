package ai.ftech.fekyc.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SDKInitRetrofitConfig : BaseRetrofitConfig() {

    override fun getUrl(): String = ApiConfig.BASE_URL_INIT_FEKYC

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(ContentTypeJson())
    }

    class ContentTypeJson : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = buildChain(chain)
            return chain.proceed(requestBuilder.build())
        }

        private fun buildChain(chain: Interceptor.Chain): Request.Builder {
            val original = chain.request()
            val builder = original.newBuilder()
            builder.addHeader(ApiConfig.HeaderName.CONTENT_TYPE, ApiConfig.HeaderValue.APPLICATION_JSON)

            builder.method(original.method, original.body)
            return builder
        }
    }
}
