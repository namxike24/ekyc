package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.data.source.remote.model.ekyc.init.sdk.RegisterEkycData
import ai.ftech.fekyc.di.RepositoryFactory

class RegisterEkycAction : BaseAction<RegisterEkycAction.RegisterEkycRV, RegisterEkycData>() {
    override suspend fun execute(rv: RegisterEkycRV): RegisterEkycData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.registerEkyc(rv.appId, rv.licenseKey)
    }

    class RegisterEkycRV(var appId: String, var licenseKey: String) : RequestValue
}
