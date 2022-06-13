package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseVH
import android.view.View
import androidx.databinding.ViewDataBinding

open class GroupVH<DATA, GROUP_DATA : GroupData<*>>(
    view: View
) : BaseVH<DATA>(view) {
    var groupData: GROUP_DATA? = null
}
