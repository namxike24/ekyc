package ai.ftech.ekyc.domain.repo

import ai.ftech.ekyc.domain.model.address.City
import ai.ftech.ekyc.domain.model.address.Nation
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo

interface IInfoRepo {
    fun getEkycInfo(): EkycInfo
    fun getCityList(): List<City>
    fun getNationList(): List<Nation>
}
