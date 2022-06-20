package ai.ftech.ekyc.di

import ai.ftech.ekyc.data.repo.EkycRepoImpl
import ai.ftech.ekyc.data.repo.InfoRepoImpl
import ai.ftech.ekyc.domain.repo.IEkycRepo
import ai.ftech.ekyc.domain.repo.IInfoRepo

object RepositoryFactory {
    private val ekycRepo = EkycRepoImpl()
    private val infoRepo = InfoRepoImpl()

    fun getEkyc(): IEkycRepo {
        return ekycRepo
    }

    fun getInfoRepo(): IInfoRepo {
        return infoRepo
    }
}
