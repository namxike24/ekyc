package ai.ftech.fekyc.data.source.remote.service

import ai.ftech.fekyc.data.source.remote.base.IApiService
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface InitSDKService : IApiService {
    @POST("/auth/sdk/init")
    fun initSDK(@Body body: InitSDKRequest): Call<InitSDKResponse>
}
