package ai.ftech.fekyc.base.common.converter

import java.util.*

class ListConverter<S, D>(
    converter: IConverter<S, D>
) : IConverter<List<S>, List<D>> {

    private val converter: IConverter<S, D> = converter

    override fun convert(source: List<S>): List<D> {
        val result: MutableList<D> = ArrayList()
        source.forEach {
            result.add(converter.convert(it))
        }
        return result
    }
}
