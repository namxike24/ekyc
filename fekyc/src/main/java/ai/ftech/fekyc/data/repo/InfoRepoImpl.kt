package ai.ftech.fekyc.data.repo

import ai.ftech.fekyc.base.common.converter.ListConverter
import ai.ftech.fekyc.base.repo.BaseRepo
import ai.ftech.fekyc.data.repo.converter.CityDataConvertToCity
import ai.ftech.fekyc.data.repo.converter.EkycDataConvertToEkycInfo
import ai.ftech.fekyc.data.repo.converter.NationDataConvertToNation
import ai.ftech.fekyc.data.source.remote.base.invokeApi
import ai.ftech.fekyc.data.source.remote.base.invokeFEkycService
import ai.ftech.fekyc.data.source.remote.service.InfoService
import ai.ftech.fekyc.domain.model.address.City
import ai.ftech.fekyc.domain.model.address.Nation
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo
import ai.ftech.fekyc.domain.repo.IInfoRepo

class InfoRepoImpl : BaseRepo(), IInfoRepo {
    override fun getEkycInfo(): EkycInfo {
        val service = invokeFEkycService(InfoService::class.java)

        return service.getUserInfo().invokeApi { _, body ->
            EkycDataConvertToEkycInfo().convert(body.data!!)
        }
    }

    override fun getCityList(): List<City> {
        val service = invokeFEkycService(InfoService::class.java)

        return service.getCityList().invokeApi { _, body ->
            ListConverter(CityDataConvertToCity()).convert(body.data!!)
        }
    }

    override fun getNationList(): List<Nation> {
        val service = invokeFEkycService(InfoService::class.java)

        return service.getNationList().invokeApi { _, body ->
            ListConverter(NationDataConvertToNation()).convert(body.data!!)
        }
    }
}
