package ai.ftech.ekyc.common.activityresultlancher

import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.FEkycFragment
import ai.ftech.ekyc.common.IFEkycContext
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

abstract class IActivityResult<I, O> {

    private var launcher: ActivityResultLauncher<I>?=null
    private var callback: ((O) -> Unit)? = null

    protected abstract fun getActivityContract(): ActivityResultContract<I, O>

    /**
     * Must call before onCreate in activity or fragment
     */
    fun register(ctx: IFEkycContext) {
        when (ctx) {
            is FEkycActivity -> {
                launcher = ctx.registerForActivityResult(getActivityContract()) {
                    callback?.invoke(it)
                }
            }
            is FEkycFragment -> {
                launcher = ctx.registerForActivityResult(getActivityContract()) {
                    callback?.invoke(it)
                }
            }
        }
    }

    fun unregister () {
        launcher = null
    }

    fun launch(input: I? = null, callback: ((O) -> Unit) = {}) {
        this.callback = callback
        launcher?.launch(input)
    }
}
