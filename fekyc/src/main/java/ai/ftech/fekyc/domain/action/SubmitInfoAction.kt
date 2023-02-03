package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.ekyc.EkycInfo
import com.google.gson.Gson

class SubmitInfoAction : BaseAction<SubmitInfoAction.SubmitRV, Boolean>() {
    override suspend fun execute(rv: SubmitRV): Boolean {
        val repo = RepositoryFactory.getEkyc()

        val gson = Gson()
        val dataAfterConvertJson = gson.toJson(rv.ekycInfo.form)

        return repo.submitInfo(dataAfterConvertJson)
    }

    class SubmitRV(val ekycInfo: EkycInfo) : RequestValue
}
