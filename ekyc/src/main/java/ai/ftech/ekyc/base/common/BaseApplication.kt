package ai.ftech.ekyc.base.common

import android.app.Application

abstract class BaseApplication : Application() {

    companion object {
        var appInstance: BaseApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}
