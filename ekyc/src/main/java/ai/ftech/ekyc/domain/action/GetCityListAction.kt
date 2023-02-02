package ai.ftech.ekyc.domain.action

import ai.ftech.ekyc.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.address.City

class GetCityListAction : BaseAction<BaseAction.VoidRequest, List<City>>() {
    override suspend fun execute(rv: VoidRequest): List<City> {
        val repo = RepositoryFactory.getInfoRepo()
        return repo.getCityList()
    }
}
