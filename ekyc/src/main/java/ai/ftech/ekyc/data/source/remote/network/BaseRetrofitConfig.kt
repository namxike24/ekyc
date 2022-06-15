package ai.ftech.ekyc.data.source.remote.network

import ai.ftech.ekyc.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitConfig {
    abstract fun getUrl(): String
    abstract fun getInterceptorList(): Array<Interceptor>

    fun getRetrofit(): Retrofit {
        return getRetrofitBuilder().build()
    }

    fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(getUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .client(provideOkHttpClient())
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder()

        builder.addInterceptor(logging)

        val interceptorList = getInterceptorList()

        interceptorList.forEach {
            builder.addInterceptor(it)
        }

        val timeout = 30L

        builder.connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
//            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), object : X509TrustManager {
//                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
//
//                }
//
//                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
//                }
//
//                override fun getAcceptedIssuers(): Array<X509Certificate> {
//                    return arrayOf()
//                }
//            })
            .hostnameVerifier { _, _ ->
                true
            }

        return builder.build()
    }

    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
//            .registerTypeAdapter(java.util.Date::class.java, GsonUtcDateAdapter())
    }
}
