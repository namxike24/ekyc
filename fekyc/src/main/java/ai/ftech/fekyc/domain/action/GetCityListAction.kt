package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.address.City

class GetCityListAction : BaseAction<BaseAction.VoidRequest, List<City>>() {
    override suspend fun execute(rv: VoidRequest): List<City> {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getCityList()
    }
}
