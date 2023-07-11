package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.facematching.FaceMatchingResponse
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData

class FaceMatchingResponseConvertData : IConverter<FaceMatchingResponse.Data?, FaceMatchingData> {
    override fun convert(source: FaceMatchingResponse.Data?): FaceMatchingData {
        if (source == null) return FaceMatchingData()
        val mapper = Mapper(FaceMatchingResponse.Data::class, FaceMatchingData::class).apply {
            register(
                FaceMatchingData::cardInfo,
                Mapper(FaceMatchingResponse.Data.CardInfo::class, FaceMatchingData.CardInfo::class)
            )
        }
        return mapper.invoke(source)
    }
}