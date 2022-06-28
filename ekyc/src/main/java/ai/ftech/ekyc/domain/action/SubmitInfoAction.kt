package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.ekyc.EkycInfo
import com.google.gson.Gson

class SubmitInfoAction : BaseAction<SubmitInfoAction.SubmitRV, Boolean>() {
    override suspend fun execute(rv: SubmitRV): Boolean {
        val repo = RepositoryFactory.getEkyc()

        val gson = Gson()
        val dataAfterConvertJson = gson.toJson(rv.ekycInfo)

        return repo.submitInfo(dataAfterConvertJson)
    }

    class SubmitRV(val ekycInfo: EkycInfo) : RequestValue
}
