package ai.ftech.ekyc.base.common.converter

interface IConverter<S, D> {
    fun convert(source: S): D
}
