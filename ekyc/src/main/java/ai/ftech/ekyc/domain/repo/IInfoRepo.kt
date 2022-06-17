package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.address.City
import ai.ftech.ekyc.domain.model.address.Nation

interface IInfoRepo {
    fun getUserInfo()
    fun getCityList(): List<City>
    fun getNationList(): List<Nation>
}
