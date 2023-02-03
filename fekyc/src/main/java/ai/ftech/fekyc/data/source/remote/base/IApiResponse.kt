package ai.ftech.fekyc.data.source.remote.base

import ai.ftech.fekyc.base.common.converter.IConverter

interface IApiResponse {
    fun isSuccessful(): Boolean

    fun <S, D> convert(source: S, converter: IConverter<S, D>): D? {
        return converter.convert(source)
    }
}
