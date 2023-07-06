package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.submit.SubmitInfo

class NewSubmitInfoAction : BaseAction<NewSubmitInfoAction.SubmitRV,SubmitInfo>() {
    override suspend fun execute(rv: SubmitRV): SubmitInfo {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.submitInfo(rv.request)
    }

    class SubmitRV(val request: NewSubmitInfoRequest) : BaseAction.RequestValue
}

