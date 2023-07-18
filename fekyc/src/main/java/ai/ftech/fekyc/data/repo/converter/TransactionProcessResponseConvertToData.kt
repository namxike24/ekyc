package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionProcessResponse
import ai.ftech.fekyc.domain.model.transaction.TransactionProcessData

class TransactionProcessResponseConvertToData :
    IConverter<TransactionProcessResponse.TransactionProcessDataResponse?, TransactionProcessData> {
    override fun convert(source: TransactionProcessResponse.TransactionProcessDataResponse?): TransactionProcessData {
        if (source == null) return TransactionProcessData()
        val mapper = Mapper(
            TransactionProcessResponse.TransactionProcessDataResponse::class,
            TransactionProcessData::class
        )
        return mapper.invoke(source)
    }
}