package ai.ftech.ekyc.common.action

import ai.ftech.dev.base.common.BaseAction

open class FEkycActionCallback<RESPONSE> : BaseAction.IActionCallback<RESPONSE> {
    override fun onSuccess(response: RESPONSE?) {
    }

    override fun onException(throwable: Throwable?) {
        throwable?.printStackTrace()
    }
}
