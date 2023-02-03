package ai.ftech.fekyc.data.source.remote.network

import ai.ftech.fekyc.domain.APIException
import ai.ftech.fekyc.publish.FTechEkycManager
import android.util.Log
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

object RetrofitFactory {
    private val TAG = RetrofitFactory::class.java.simpleName
    private const val FEKYC = "FEKYC"

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    fun <T> createFEkycService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            val builderInfo = RetrofitBuilderInfo().apply {
                    this.ftechKey = FTechEkycManager.ftechKey
                    this.appID = FTechEkycManager.appId
                    this.transactionId = FTechEkycManager.transactionId
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
            Log.e(TAG, "Reuse domain retrofit builder for ${ApiConfig.BASE_URL_FEKYC}")
            val serviceApi = builderInfo.builder?.build()?.create(service)

            return serviceApi ?: throw APIException(APIException.CREATE_INSTANCE_SERVICE_ERROR)
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
