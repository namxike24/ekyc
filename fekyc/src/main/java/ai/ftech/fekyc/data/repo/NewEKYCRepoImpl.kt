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

class NewEKYCRepoImpl : BaseRepo(), INewEKYCRepo {
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

}
