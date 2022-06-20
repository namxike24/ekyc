package ai.ftech.ekyc.data.source.remote.service

import ai.ftech.ekyc.data.source.remote.model.city.CityResponse
import ai.ftech.ekyc.data.source.remote.model.nation.NationResponse
import ai.ftech.ekyc.data.source.remote.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface InfoService {
    @GET("v1/ekyc/getInfo")
    fun getUserInfo(): Call<UserResponse>

    @GET("/v1/ekyc/listCountry")
    fun getCityList(): Call<CityResponse>

    @GET("/v1/ekyc/listNational")
    fun getNationList(): Call<NationResponse>
}
