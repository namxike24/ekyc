package ai.ftech.ekyc.data.source.remote.service

import ai.ftech.ekyc.data.source.remote.model.CaptureFaceResponse
import ai.ftech.ekyc.data.source.remote.model.UploadRequest
import ai.ftech.ekyc.data.source.remote.model.VerifyIdentityResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EkycService {
    @Multipart
    @POST("/v1/ekyc/passport")
    fun verifyIdentityPassport(@Part file: MultipartBody.Part): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/nationId")
    fun verifyIdentityFront(@Part file: MultipartBody.Part, @Body body: UploadRequest): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/nationId")
    fun verifyIdentityBack(@Part file: MultipartBody.Part, @Body body: UploadRequest): Call<VerifyIdentityResponse>

    @Multipart
    @POST("/v1/ekyc/captureFace")
    fun captureFace(@Part file: MultipartBody.Part): Call<CaptureFaceResponse>
}
