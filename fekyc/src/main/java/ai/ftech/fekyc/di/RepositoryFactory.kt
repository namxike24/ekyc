package ai.ftech.fekyc.di

import ai.ftech.fekyc.data.repo.EkycRepoImpl
import ai.ftech.fekyc.data.repo.InfoRepoImpl
import ai.ftech.fekyc.domain.repo.IEkycRepo
import ai.ftech.fekyc.domain.repo.IInfoRepo

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
