package ai.ftech.ekyc.data.repo.converter

import ai.ftech.dev.base.common.converter.IConverter
import ai.ftech.dev.base.common.converter.Mapper
import ai.ftech.ekyc.data.source.remote.model.nation.NationData
import ai.ftech.ekyc.domain.model.address.Nation

class NationDataConvertToNation : IConverter<NationData, Nation> {
    override fun convert(source: NationData): Nation {
        val mapper = Mapper(NationData::class, Nation::class)
        return mapper.invoke(source)
    }
}
