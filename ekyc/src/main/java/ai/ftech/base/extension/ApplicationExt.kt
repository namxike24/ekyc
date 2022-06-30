package ai.ftech.base.extension

import ai.ftech.base.common.BasePreference
import android.app.Application

private var application: Application? = null

fun setApplication(context: Application) {
    application = context
}

fun getApplication() = application

private var basePreference: BasePreference? = null

fun Application.initPrefData(preferenceName: String) {
    basePreference = BasePreference(preferenceName, this)
}

fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
    basePreference!!.get(key, defaultValue, this)

fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)
