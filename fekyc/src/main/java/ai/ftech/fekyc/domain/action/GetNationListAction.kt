package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.address.Nation

class GetNationListAction : BaseAction<BaseAction.VoidRequest, List<Nation>>() {
    override suspend fun execute(rv: VoidRequest): List<Nation> {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getNationList()
    }
}
