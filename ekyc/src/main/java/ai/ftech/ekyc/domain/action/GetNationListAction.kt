package ai.ftech.ekyc.domain.action

import ai.ftech.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.address.Nation

class GetNationListAction : BaseAction<BaseAction.VoidRequest, List<Nation>>() {
    override suspend fun execute(rv: VoidRequest): List<Nation> {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getNationList()
    }
}
