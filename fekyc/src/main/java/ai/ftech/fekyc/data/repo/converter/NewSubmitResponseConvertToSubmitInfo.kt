package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.submit.NewSubmitInfoResponse
import ai.ftech.fekyc.domain.model.submit.SubmitInfo

class NewSubmitResponseConvertToSubmitInfo : IConverter<NewSubmitInfoResponse, SubmitInfo> {
    override fun convert(source: NewSubmitInfoResponse): SubmitInfo {
        val mapper = Mapper(NewSubmitInfoResponse::class, SubmitInfo::class)
        return mapper.invoke(source)
    }
}