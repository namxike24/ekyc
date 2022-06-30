package ai.ftech.base.adapter.group

import ai.ftech.base.adapter.BaseVH
import android.view.View

open class GroupVH<DATA, GROUP_DATA : GroupData<*>>(view: View) : BaseVH<DATA>(view) {
    var groupData: GROUP_DATA? = null
}