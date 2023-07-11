package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.di.RepositoryFactory

class NewSubmitInfoAction : BaseAction<NewSubmitInfoAction.SubmitRV,Boolean>() {
    override suspend fun execute(rv: SubmitRV): Boolean {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.submitInfo(rv.request)
    }

    class SubmitRV(val request: NewSubmitInfoRequest) : RequestValue
}

