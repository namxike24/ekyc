package ai.ftech.fekyc.domain.repo

import ai.ftech.fekyc.domain.model.address.City
import ai.ftech.fekyc.domain.model.address.Nation
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo

interface IInfoRepo {
    fun getEkycInfo(): EkycInfo
    fun getCityList(): List<City>
    fun getNationList(): List<Nation>
}
