package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.Converter
import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.capture.CaptureResponse
import ai.ftech.fekyc.domain.model.capture.CaptureData

class CapturePhotoResponseConvertToData : IConverter<CaptureResponse, CaptureData> {
    override fun convert(source: CaptureResponse): CaptureData {
        val sessionDataMapper =
            Mapper(CaptureResponse.DataCapture::class, CaptureData.SessionData::class).apply {
                addNameMapper(CaptureData.SessionData::sessionId) {
                    return@addNameMapper CaptureResponse.DataCapture::sessionId
                }
                register(CaptureData.SessionData::sessionId, object : Converter<String, String> {
                    override fun invoke(p1: String): String {
                        return p1
                    }
                })
            }
        val mapper = Mapper(CaptureResponse::class, CaptureData::class).apply {
            register(CaptureData::data, sessionDataMapper)
        }
        return mapper.invoke(source)
    }
}