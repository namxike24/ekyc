package ai.ftech.fekyc.data.source.remote.service

import ai.ftech.fekyc.data.source.remote.base.IApiService
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface NewEkycService : IApiService {
    @POST("/ekyc/transaction/")
    fun createTransaction(@Body body: TransactionRequest): Call<TransactionResponse>

    @Multipart
    @POST("/ekyc/card/")
    fun uploadIdentityCard(@Query("transaction_id") transactionId: String,
                           @Query("card_orientation") type: String,
                           @Part uploadFile: MultipartBody.Part): Call<TransactionResponse>
}
