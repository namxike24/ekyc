package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.Converter
import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoRequest
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData

class FaceMatchingDataConvertToSubmitRequest : IConverter<FaceMatchingData, NewSubmitInfoRequest> {
    override fun convert(source: FaceMatchingData): NewSubmitInfoRequest {
        val mapper = Mapper(FaceMatchingData::class, NewSubmitInfoRequest::class).apply {
            addNameMapper(NewSubmitInfoRequest::preProcessId) {
                return@addNameMapper FaceMatchingData::sessionId
            }
            register(NewSubmitInfoRequest::preProcessId, object : Converter<String, String>{
                override fun invoke(p1: String): String {
                    return p1
                }

            })
            addNameMapper(NewSubmitInfoRequest::cardInfoSubmit) {
                return@addNameMapper FaceMatchingData::cardInfo
            }
            register(NewSubmitInfoRequest::cardInfoSubmit, object : Converter<FaceMatchingData.CardInfo,NewSubmitInfoRequest.CardInfoSubmit>{
                override fun invoke(p1: FaceMatchingData.CardInfo): NewSubmitInfoRequest.CardInfoSubmit {
                    val mapper = Mapper(FaceMatchingData.CardInfo::class, NewSubmitInfoRequest.CardInfoSubmit::class)
                    return mapper.invoke(p1)
                }
            })

        }
        return mapper.invoke(source)
    }
}