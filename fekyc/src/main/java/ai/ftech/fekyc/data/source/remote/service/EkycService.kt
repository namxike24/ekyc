package ai.ftech.fekyc.data.source.remote.service

import ai.ftech.fekyc.data.source.remote.base.BaseApiResponse
import ai.ftech.fekyc.data.source.remote.base.IApiService
import ai.ftech.fekyc.data.source.remote.model.CaptureFaceResponse
import ai.ftech.fekyc.data.source.remote.model.VerifyIdentityResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.SubmitInfoRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EkycService : IApiService {
    @Multipart
    @POST("/v1/ekyc/nationId")
    fun verifyIdentitySSN(@Part file: MultipartBody.Part, @PartMap map: HashMap<String, RequestBody>): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/driverlicense")
    fun verifyIdentityDriverLicense(@Part file: MultipartBody.Part, @PartMap map: HashMap<String, RequestBody>): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/passport")
    fun verifyIdentityPassport(@Part file: MultipartBody.Part): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/captureFace")
    fun captureFace(@Part file: MultipartBody.Part): Call<CaptureFaceResponse>

    @POST("/v1/ekyc/submitInfo")
    fun submitInfo(@Body body: SubmitInfoRequest): Call<BaseApiResponse>
}
