package ai.ftech.demo

import ai.ftech.fekyc.publish.FTechEkycManager
import android.app.Application

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FTechEkycManager.init(this)
    }
}
