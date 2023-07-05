package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.InitSDKData
import ai.ftech.fekyc.di.RepositoryFactory

class InitSDKAction : BaseAction<InitSDKAction.InitSDKRV, InitSDKData>() {
    override suspend fun execute(rv: InitSDKRV): InitSDKData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.initSDK(rv.appId, rv.licenseKey)
    }

    class InitSDKRV(var appId: String, var licenseKey: String) : RequestValue
}
