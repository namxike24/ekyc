package ai.ftech.fekyc.domain.repo

import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.RegisterEkycData
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.domain.model.capture.CaptureData
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData

interface INewEKYCRepo {
    fun registerEkyc(appId: String, licenseKey: String): RegisterEkycData

    fun createTransaction(extraData: String): TransactionData

    fun submitInfo(request: NewSubmitInfoRequest): Boolean

    fun capturePhoto(transactionId: String, orientation: String, imagePath: String):CaptureData

    fun captureFace(transactionId: String, imagePath: String): CaptureData

    fun faceMatching(
        idTransaction: String,
        idSessionFront: String,
        idSessionBack: String,
        idSessionFace: String
    ): FaceMatchingData
}
