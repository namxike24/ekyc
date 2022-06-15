package ai.ftech.ekyc.common

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
import android.content.Context

interface IFEkycContext {
    fun getActivityContext(): Context
    fun showLoading(message: String = getAppString(R.string.fekyc_loading))
    fun hideLoading()
    fun showError(msg: String?)
    fun showSuccess(msg: String?)
    fun showWarning(msg: String?)
}