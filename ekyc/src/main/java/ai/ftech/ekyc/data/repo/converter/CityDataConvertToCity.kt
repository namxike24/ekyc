package ai.ftech.ekyc.data.repo.converter

import ai.ftech.ekyc.base.common.converter.IConverter
import ai.ftech.ekyc.base.common.converter.Mapper
import ai.ftech.ekyc.data.source.remote.model.city.CityData
import ai.ftech.ekyc.domain.model.address.City

class CityDataConvertToCity : IConverter<CityData, City> {
    override fun convert(source: CityData): City {
        val mapper = Mapper(CityData::class, City::class)
        return mapper.invoke(source)
    }
}
