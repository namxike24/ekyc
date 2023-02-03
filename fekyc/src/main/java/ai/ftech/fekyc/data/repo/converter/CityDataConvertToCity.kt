package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.city.CityData
import ai.ftech.fekyc.domain.model.address.City

class CityDataConvertToCity : IConverter<CityData, City> {
    override fun convert(source: CityData): City {
        val mapper = Mapper(CityData::class, City::class)
        return mapper.invoke(source)
    }
}
