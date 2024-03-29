package ai.ftech.fekyc.data.repo.converter

import ai.ftech.fekyc.base.common.converter.Converter
import ai.ftech.fekyc.base.common.converter.IConverter
import ai.ftech.fekyc.base.common.converter.Mapper
import ai.ftech.fekyc.data.source.remote.model.ekyc.EkycData
import ai.ftech.fekyc.data.source.remote.model.ekyc.EkycFormData
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo
import ai.ftech.fekyc.domain.model.ekyc.EkycFormInfo
import android.util.Log

class EkycDataConvertToEkycInfo : IConverter<EkycData, EkycInfo> {
    private val formMapper = Mapper(EkycFormData::class, EkycFormInfo::class).apply {
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
                return if (p1 != null) {
                    EkycFormInfo.FIELD_TYPE.valueOfName(p1)
                } else {
                    Log.e("FIELD_TYPE", "`FIELD_TYPE` response form server is null")
                    null
                }
            }
        })

        register(EkycFormInfo::dateType, object : Converter<Int?, EkycFormInfo.DATE_TYPE?> {
            override fun invoke(p1: Int?): EkycFormInfo.DATE_TYPE? {
                return if (p1 != null) {
                    EkycFormInfo.DATE_TYPE.valueOfName(p1)
                } else {
                    Log.e("FIELD_TYPE", "`DATE_TYPE` response form server is null")
                    null
                }
            }
        })
    }

    override fun convert(source: EkycData): EkycInfo {
        val mapper = Mapper(EkycData::class, EkycInfo::class).apply {
            register(EkycInfo::form, Mapper.listMapper(formMapper))
        }
        return mapper.invoke(source)
    }
}
