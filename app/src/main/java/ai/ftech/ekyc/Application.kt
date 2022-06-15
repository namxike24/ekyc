package ai.ftech.ekyc

import ai.ftech.dev.base.common.BaseApplication

class Application : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        FTechEkycManager.init(this)
    }

}
