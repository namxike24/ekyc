package ai.ftech.ekyc.data.repo.converter

import ai.ftech.dev.base.common.converter.Converter
import ai.ftech.dev.base.common.converter.Mapper
import ai.ftech.ekyc.data.source.remote.model.ekyc.FormData
import ai.ftech.ekyc.domain.model.ekyc.FormInfo

object Mappers {
    object EkycMapper {
        val formMapper = Mapper(FormData::class, FormInfo::class).apply {
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
        }
    }
}
