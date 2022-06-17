package ai.ftech.ekyc.domain.action

import ai.ftech.dev.base.common.BaseAction
import ai.ftech.ekyc.di.RepositoryFactory
import ai.ftech.ekyc.domain.model.UserInfo

class GetUserInfoAction : BaseAction<BaseAction.VoidRequest, List<UserInfo>>() {
    override suspend fun execute(rv: VoidRequest): List<UserInfo> {
        val repo = RepositoryFactory.getInfoRepo()


        return repo.getUserInfo()
    }
}
