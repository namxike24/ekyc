package ai.ftech.ekyc.data.repo.converter.ekyc

import ai.ftech.dev.base.common.converter.IConverter
import ai.ftech.dev.base.common.converter.Mapper
import ai.ftech.ekyc.data.repo.converter.Mappers
import ai.ftech.ekyc.data.source.remote.model.ekyc.EkycData
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo

class EkycDataConvertToEkycInfo : IConverter<EkycData, EkycInfo> {
    override fun convert(source: EkycData): EkycInfo {
        val mapper = Mapper(EkycData::class, EkycInfo::class).apply {
            register(EkycInfo::formList, Mapper.listMapper(Mappers.EkycMapper.formMapper))
        }
        return mapper.invoke(source)
    }
}
