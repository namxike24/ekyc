package ai.ftech.fekyc.data.source.remote.service

import ai.ftech.fekyc.data.source.remote.base.IApiService
import ai.ftech.fekyc.data.source.remote.model.ekyc.capture.CaptureResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.facematching.FaceMatchingRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.facematching.FaceMatchingResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionProcessResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NewEkycService : IApiService {
    @POST("/ekyc/transaction/")
    fun createTransaction(@Body body: TransactionRequest): Call<TransactionResponse>

    @POST("/ekyc/submit/")
    fun submitInfo(@Body body: NewSubmitInfoRequest): Call<NewSubmitInfoResponse>

    @Multipart
    @POST("/ekyc/card/")
    fun capturePhoto(
        @Part file: MultipartBody.Part,
        @PartMap transactionId: HashMap<String, RequestBody>,
        @PartMap cardOrientation: HashMap<String, RequestBody>
    ): Call<CaptureResponse>

    @Multipart
    @POST("/ekyc/face/")
    fun captureFace(
        @Part file: MultipartBody.Part,
        @PartMap transactionId: HashMap<String, RequestBody>
    ): Call<CaptureResponse>

    @POST("/ekyc/process/")
    fun faceMatching(@Body body: FaceMatchingRequest): Call<FaceMatchingResponse>

    @Multipart
    @POST("/ekyc/card/")
    fun uploadIdentityCard(@Query("transaction_id") transactionId: String,
                           @Query("card_orientation") type: String,
                           @Part uploadFile: MultipartBody.Part): Call<TransactionResponse>

    @GET("/ekyc/transaction/{transaction_id}")
    fun getProcessTransaction(
        @Path(value = "transaction_id") transactionId: String
    ): Call<TransactionProcessResponse>
}
