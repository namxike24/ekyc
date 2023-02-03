package ai.ftech.fekyc.base.common.converter

interface IConverter<S, D> {
    fun convert(source: S): D
}
