package ai.ftech.ekyc.common

import ai.ftech.dev.base.common.BaseActivity

abstract class FEkycActivity(layoutId: Int) : BaseActivity(layoutId) {

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        setFullScreen()
    }
}
