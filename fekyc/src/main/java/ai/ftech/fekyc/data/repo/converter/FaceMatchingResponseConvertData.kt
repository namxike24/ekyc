package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.facematching.FaceMatchingResponse
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData

class FaceMatchingResponseConvertData : IConverter<FaceMatchingResponse, FaceMatchingData> {
    override fun convert(source: FaceMatchingResponse): FaceMatchingData {
        val mapper = Mapper(FaceMatchingResponse::class, FaceMatchingData::class)
        return mapper.invoke(source)
    }
}