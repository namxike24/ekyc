package ai.ftech.ekyc.di

import ai.ftech.ekyc.data.repo.EkycImpl
import ai.ftech.ekyc.domain.repo.IEkycRepo

object RepositoryFactory {
    private val ekycRepo = EkycImpl()

    fun getEkyc(): IEkycRepo {
        return ekycRepo
    }
}
