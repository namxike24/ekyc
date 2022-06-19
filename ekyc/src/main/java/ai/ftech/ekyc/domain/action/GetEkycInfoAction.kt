package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import android.util.Log

class GetEkycInfoAction : BaseAction<BaseAction.VoidRequest, EkycInfo>() {
    override suspend fun execute(rv: VoidRequest): EkycInfo {
        val repo = RepositoryFactory.getInfoRepo()
        val x = repo.getEkycInfo()
        Log.d("anhnd", "execute: $x")
        return x
    }
}
