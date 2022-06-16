package ai.ftech.ekyc.data.source.remote.network

import android.util.Log
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

object RetrofitFactory {
    private val TAG = RetrofitFactory::class.java.simpleName
    private const val FEKYC = "FEKYC"

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    fun <T> createFEkycService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            var builderInfo = builderMap[FEKYC]
            if (builderInfo == null) {

                // TODO: hardcode tạm vì chưa thiết kế các func cho app truyền vào các giá trị này
                builderInfo = RetrofitBuilderInfo().apply {
                    this.ftechKey = "123"
                    this.appID = "111"
                    this.transactionId = "12345"
                    this.language = ApiConfig.API_LANGUAGE.VI
                }

                builderInfo.builder = EkycRetrofitConfig(
                    builderInfo.ftechKey,
                    builderInfo.appID,
                    builderInfo.transactionId,
                    builderInfo.language.language
                ).getRetrofitBuilder()

                builderMap[FEKYC] = builderInfo
                Log.d(TAG, "Create new domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
            }
            Log.e(TAG, "Reuse domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
            var serviceApi: T? = null
            try {
                serviceApi = builderInfo.builder?.build()?.create(service)

            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
            return serviceApi!!
        }
    }

    class RetrofitBuilderInfo {
        var builder: Retrofit.Builder? = null
        var ftechKey: String = ""
        var appID: String = ""
        var transactionId: String = ""
        var language: ApiConfig.API_LANGUAGE = ApiConfig.API_LANGUAGE.VI
    }
}
