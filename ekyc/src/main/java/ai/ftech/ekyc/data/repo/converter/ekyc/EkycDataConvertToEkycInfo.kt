package ai.ftech.ekyc.data.repo.converter.ekyc

import ai.ftech.dev.base.common.converter.Converter
import ai.ftech.dev.base.common.converter.IConverter
import ai.ftech.dev.base.common.converter.Mapper
import ai.ftech.ekyc.data.source.remote.model.ekyc.EkycData
import ai.ftech.ekyc.data.source.remote.model.ekyc.FormData
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import ai.ftech.ekyc.domain.model.ekyc.FormInfo

class EkycDataConvertToEkycInfo : IConverter<EkycData, EkycInfo> {
    private val formMapper = Mapper(FormData::class, FormInfo::class).apply {
        addNameMapper(FormInfo::title) {
            return@addNameMapper FormData::fieldName
        }

        addNameMapper(FormInfo::value) {
            return@addNameMapper FormData::fieldValue
        }

        addNameMapper(FormInfo::type) {
            return@addNameMapper FormData::fieldType
        }

        addNameMapper(FormInfo::isEditable) {
            return@addNameMapper FormData::editable
        }
        register(FormInfo::isEditable, object : Converter<Boolean?, Boolean> {
            override fun invoke(p1: Boolean?): Boolean {
                return p1 ?: false
            }
        })

        register(FormInfo::fieldType, object : Converter<String?, FormInfo.FIELD_TYPE?> {
            override fun invoke(p1: String?): FormInfo.FIELD_TYPE? {
                return FormInfo.FIELD_TYPE.valueOfName(p1)
            }
        })
    }

    override fun convert(source: EkycData): EkycInfo {
        val mapper = Mapper(EkycData::class, EkycInfo::class).apply {
            register(EkycInfo::formList, Mapper.listMapper(formMapper))
        }
        return mapper.invoke(source)
    }
}
