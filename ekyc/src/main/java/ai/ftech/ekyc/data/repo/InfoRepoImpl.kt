package ai.ftech.ekyc.data.repo

import ai.ftech.dev.base.repo.BaseRepo
import ai.ftech.ekyc.domain.model.UserInfo
import ai.ftech.ekyc.domain.model.address.City
import ai.ftech.ekyc.domain.model.address.Nation
import ai.ftech.ekyc.domain.repo.IInfoRepo

class InfoRepoImpl : BaseRepo(), IInfoRepo {
    override fun getUserInfo(): List<UserInfo> {
        TODO("Not yet implemented")
    }

    override fun getCityList(): List<City> {
        TODO("Not yet implemented")
    }

    override fun getNationList(): List<Nation> {
        TODO("Not yet implemented")
    }
}
