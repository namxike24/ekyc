package ai.ftech.fekyc.domain.action

import ai.ftech.fekyc.base.common.BaseAction
import ai.ftech.fekyc.di.RepositoryFactory
import ai.ftech.fekyc.domain.model.facematching.FaceMatchingData

class FaceMatchingAction : BaseAction<FaceMatchingAction.FaceMatchingRV, FaceMatchingData>() {
    class FaceMatchingRV(
        var idTransaction: String,
        var idSessionFront: String,
        var idSessionBack: String,
        var idSessionFace: String
    ) : RequestValue

    override suspend fun execute(rv: FaceMatchingRV): FaceMatchingData {
        val repo = RepositoryFactory.getNewEKYCRepo()
        return repo.faceMatching(
            rv.idTransaction,
            rv.idSessionFront,
            rv.idSessionBack,
            rv.idSessionFace
        )
    }
}