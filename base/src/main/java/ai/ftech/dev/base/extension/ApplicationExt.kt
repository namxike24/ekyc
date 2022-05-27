package ai.ftech.dev.base.extension

import ai.ftech.dev.base.common.BaseApplication
import ai.ftech.dev.base.common.BasePreference
import android.app.Application

fun getApplication() = BaseApplication.appInstance!!

private var basePreference: BasePreference? = null

fun Application.initPrefData(preferenceName: String) {
    basePreference = BasePreference(preferenceName, this)
}

fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
    basePreference!!.get(key, defaultValue, this)

fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)
