package ai.ftech.ekyc.data.repo.converter.ekyc

import ai.ftech.dev.base.common.converter.Converter
import ai.ftech.dev.base.common.converter.IConverter
import ai.ftech.dev.base.common.converter.Mapper
import ai.ftech.ekyc.data.source.remote.model.ekyc.EkycData
import ai.ftech.ekyc.data.source.remote.model.ekyc.EkycFormData
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import ai.ftech.ekyc.domain.model.ekyc.EkycFormInfo

class EkycDataConvertToEkycInfo : IConverter<EkycData, EkycInfo> {
    private val formMapper = Mapper(EkycFormData::class, EkycFormInfo::class).apply {
        addNameMapper(EkycFormInfo::title) {
            return@addNameMapper EkycFormData::fieldName
        }

        addNameMapper(EkycFormInfo::value) {
            return@addNameMapper EkycFormData::fieldValue
        }

        addNameMapper(EkycFormInfo::type) {
            return@addNameMapper EkycFormData::fieldType
        }

        addNameMapper(EkycFormInfo::isEditable) {
            return@addNameMapper EkycFormData::editable
        }
        register(EkycFormInfo::isEditable, object : Converter<Boolean?, Boolean> {
            override fun invoke(p1: Boolean?): Boolean {
                return p1 ?: false
            }
        })

        register(EkycFormInfo::fieldType, object : Converter<String?, EkycFormInfo.FIELD_TYPE?> {
            override fun invoke(p1: String?): EkycFormInfo.FIELD_TYPE? {
                return EkycFormInfo.FIELD_TYPE.valueOfName(p1)
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
