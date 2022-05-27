package ai.ftech.dev.base.common.converter

interface IConverter<S, D> {
    fun convert(source: S): D
}
