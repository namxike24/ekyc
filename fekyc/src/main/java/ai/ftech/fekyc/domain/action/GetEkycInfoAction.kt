package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo

class GetEkycInfoAction : BaseAction<BaseAction.VoidRequest, EkycInfo>() {
    override suspend fun execute(rv: VoidRequest): EkycInfo {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getEkycInfo()
    }
}
