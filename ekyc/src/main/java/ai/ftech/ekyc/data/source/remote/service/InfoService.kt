package ai.ftech.ekyc.data.source.remote.service

import ai.ftech.ekyc.data.source.remote.base.IApiService
import ai.ftech.ekyc.data.source.remote.model.city.CityResponse
import ai.ftech.ekyc.data.source.remote.model.nation.NationResponse
import ai.ftech.ekyc.data.source.remote.model.ekyc.EkycResponse
import retrofit2.Call
import retrofit2.http.GET

interface InfoService : IApiService {
    @GET("v1/ekyc/getInfo")
    fun getUserInfo(): Call<EkycResponse>

    @GET("/v1/ekyc/listCountry")
    fun getCityList(): Call<CityResponse>

    @GET("/v1/ekyc/listNational")
    fun getNationList(): Call<NationResponse>
}
