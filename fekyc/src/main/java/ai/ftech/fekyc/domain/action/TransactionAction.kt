package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.data.source.remote.model.ekyc.transaction.TransactionData
import ai.ftech.fekyc.di.RepositoryFactory

class TransactionAction : BaseAction<BaseAction.VoidRequest, TransactionData>() {
    companion object {
        const val EXTRA_DATA = "uuid"
    }

    override suspend fun execute(rv: VoidRequest): TransactionData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.createTransaction(EXTRA_DATA)
    }
}
