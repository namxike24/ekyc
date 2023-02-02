package ai.ftech.ekyc.domain.action

import ai.ftech.ekyc.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo

class GetEkycInfoAction : BaseAction<BaseAction.VoidRequest, EkycInfo>() {
    override suspend fun execute(rv: VoidRequest): EkycInfo {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getEkycInfo()
    }
}
