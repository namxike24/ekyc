package ai.ftech.demo

import ai.ftech.dev.base.common.BaseApplication
import ai.ftech.ekyc.FTechEkycManager

class Application : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        FTechEkycManager.init(this)
    }

}
