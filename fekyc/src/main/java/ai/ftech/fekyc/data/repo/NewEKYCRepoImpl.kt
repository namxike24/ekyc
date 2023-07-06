package ai.ftech.fekyc.data.repo

import ai.ftech.fekyc.base.repo.BaseRepo
import ai.ftech.fekyc.data.repo.converter.NewSubmitResponseConvertToSubmitInfo
import ai.ftech.fekyc.data.source.remote.base.invokeApi
import ai.ftech.fekyc.data.source.remote.base.invokeInitSDKFEkycService
import ai.ftech.fekyc.data.source.remote.base.invokeNewFEkycService
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKData
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionRequest
import ai.ftech.fekyc.data.source.remote.service.InitSDKService
import ai.ftech.fekyc.data.source.remote.service.NewEkycService
import ai.ftech.fekyc.domain.model.submit.SubmitInfo
import ai.ftech.fekyc.domain.repo.INewEKYCRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NewEKYCRepoImpl : BaseRepo(), INewEKYCRepo {

    companion object {
        private const val PART_IMAGE = "image"
        private const val PART_ORIENTATION = "card_orientation"
        private const val PART_TRANSACTION_ID = "transaction_id"
    }

    override fun initSDK(appId: String, licenseKey: String): InitSDKData {
        val service = invokeInitSDKFEkycService(InitSDKService::class.java)

        val request = InitSDKRequest().apply {
            this.appId = appId
            this.secretKey = licenseKey
        }

        return service.initSDK(request).invokeApi { _, body -> body.data!! }
    }

    override fun createTransaction(extraData: String): TransactionData {
        val service = invokeNewFEkycService(NewEkycService::class.java)

        val request = TransactionRequest().apply {
            this.extraData = extraData
        }
        return service.createTransaction(request).invokeApi { _, body -> body.data!! }
    }

    override fun submitInfo(request: NewSubmitInfoRequest): SubmitInfo {
        val service = invokeNewFEkycService(NewEkycService::class.java)

        return service.submitInfo(request).invokeApi { _, body ->
            NewSubmitResponseConvertToSubmitInfo().convert(body)
        }
    }

    override fun capturePhoto(
        transactionId: String,
        orientation: String,
        imagePath: String
    ): Boolean {
        val service = invokeNewFEkycService(NewEkycService::class.java)

        val partImage = convertImageToMultipart(imagePath)
        val partTransactionId =
            hashMapOf(PART_TRANSACTION_ID to convertToRequestBody(transactionId))
        val partOrientation = hashMapOf(PART_ORIENTATION to convertToRequestBody(orientation))

        return service.capturePhoto(partImage, partTransactionId, partOrientation)
            .invokeApi { _, _ -> true }
    }

    override fun captureFace(transactionId: String, imagePath: String): Boolean {
        val service = invokeNewFEkycService(NewEkycService::class.java)

        val partImage = convertImageToMultipart(imagePath)
        val partTransactionId =
            hashMapOf(PART_TRANSACTION_ID to convertToRequestBody(transactionId))

        return service.captureFace(partImage, partTransactionId).invokeApi { _, _ -> true }
    }

    private fun convertImageToMultipart(absolutePath: String): MultipartBody.Part {
        val file = File(absolutePath)
        return MultipartBody.Part.createFormData(PART_IMAGE, file.name, file.asRequestBody())
    }

    private fun convertToRequestBody(field: String): RequestBody {
        return field.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}
