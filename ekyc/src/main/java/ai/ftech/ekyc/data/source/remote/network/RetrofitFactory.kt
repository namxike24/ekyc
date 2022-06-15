package ai.ftech.ekyc.data.source.remote.network

import android.util.Log
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

object RetrofitFactory {
    private val TAG = RetrofitFactory::class.java.simpleName
    private const val FEKYC = "FEKYC"

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    fun <T> createFEkycService(service: Class<T>): T? {
        synchronized(RetrofitBuilderInfo::class.java) {
            var builderInfo = builderMap[FEKYC]
            if (builderInfo == null) {

                builderInfo = RetrofitBuilderInfo()

                if (builderInfo.ftechKey != null && builderInfo.appID != null && builderInfo.transactionId != null && builderInfo.language?.language != null) {

                    builderInfo.builder = EkycRetrofitConfig(
                        builderInfo.ftechKey!!,
                        builderInfo.appID!!,
                        builderInfo.transactionId!!,
                        builderInfo.language?.language!!
                    ).getRetrofitBuilder()

                    builderMap[FEKYC] = builderInfo
                    Log.d(TAG, "Create new domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
                }

            }
            Log.e(TAG, "Reuse domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
            return builderInfo.builder?.build()?.create(service)
        }
    }

    class RetrofitBuilderInfo {
        var builder: Retrofit.Builder? = null
        var ftechKey: String? = null
        var appID: String? = null
        var transactionId: String? = null
        var language: API_LANGUAGE? = null
    }
}
