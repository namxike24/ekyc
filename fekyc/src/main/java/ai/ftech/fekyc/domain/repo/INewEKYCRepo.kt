package ai.ftech.fekyc.domain.repo

import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKData
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData
import ai.ftech.fekyc.domain.model.submit.SubmitInfo

interface INewEKYCRepo {
    fun initSDK(appId: String, licenseKey: String): InitSDKData

    fun createTransaction(extraData: String): TransactionData

    fun submitInfo(request: NewSubmitInfoRequest): SubmitInfo

    fun capturePhoto(transactionId: String, orientation: String, imagePath: String): Boolean

    fun captureFace(transactionId: String, imagePath: String): Boolean

    fun faceMatching(
        idTransaction: String,
        idSessionFront: String,
        idSessionBack: String,
        idSessionFace: String
    ): FaceMatchingData
}
