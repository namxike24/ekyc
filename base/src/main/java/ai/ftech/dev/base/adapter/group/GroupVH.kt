package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseVH
import androidx.databinding.ViewDataBinding

open class GroupVH<DATA, GROUP_DATA : GroupData<*>>(
    viewDataBinding: ViewDataBinding
) : BaseVH<DATA>(viewDataBinding) {
    var groupData: GROUP_DATA? = null
}
