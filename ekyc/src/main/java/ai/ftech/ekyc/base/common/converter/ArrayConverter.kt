package ai.ftech.ekyc.base.common.converter

import java.util.*

class ArrayConverter<S, D>(
    private val converter: IConverter<S, D>
) : IConverter<Array<S>?, List<D>?> {

    override fun convert(source: Array<S>?): List<D> {
        val result: MutableList<D> = ArrayList()
        source?.forEach {
            result.add(converter.convert(it))
        }
        return result
    }
}
