package ai.ftech.ekyc.di

import ai.ftech.ekyc.data.repo.EkycRepoImpl
import ai.ftech.ekyc.domain.repo.IEkycRepo

object RepositoryFactory {
    private val ekycRepo = EkycRepoImpl()

    fun getEkyc(): IEkycRepo {
        return ekycRepo
    }
}
