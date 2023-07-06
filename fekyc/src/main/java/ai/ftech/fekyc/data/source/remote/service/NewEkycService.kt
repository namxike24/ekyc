package ai.ftech.fekyc.data.source.remote.service

import ai.ftech.fekyc.data.source.remote.base.IApiService
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoResponse
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NewEkycService : IApiService {
    @POST("/ekyc/transaction/")
    fun createTransaction(@Body body: TransactionRequest): Call<TransactionResponse>

    @POST("/ekyc/submit/")
    fun submitInfo(@Body body: NewSubmitInfoRequest): Call<NewSubmitInfoResponse>
}
