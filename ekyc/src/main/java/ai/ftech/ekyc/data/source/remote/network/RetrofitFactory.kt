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
                Log.d(TAG, "Create new domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
                builderInfo = RetrofitBuilderInfo()
                builderInfo.builder = EkycRetrofitConfig().getRetrofitBuilder()
                builderMap[FEKYC] = builderInfo
            }
            Log.e(TAG, "Reuse domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
            return builderInfo.builder?.build()?.create(service)
        }
    }

    class RetrofitBuilderInfo {
        var builder: Retrofit.Builder? = null
    }
}
