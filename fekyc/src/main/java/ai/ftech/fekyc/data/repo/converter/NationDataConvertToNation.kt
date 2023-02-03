package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.nation.NationData
import ai.ftech.fekyc.domain.model.address.Nation

class NationDataConvertToNation : IConverter<NationData, Nation> {
    override fun convert(source: NationData): Nation {
        val mapper = Mapper(NationData::class, Nation::class)
        return mapper.invoke(source)
    }
}
