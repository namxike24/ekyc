package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseVH
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter : RecyclerView.Adapter<BaseVH<*>>() {

    val groupManager: GroupManager = GroupManager(this)
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<*> {
        context = parent.context

        val layoutResource = groupManager.getLayoutResource(viewType)

        if (hasBindingView()) {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(context),
                layoutResource,
                parent,
                false
            )
            if (binding != null) {
                return groupManager.onCreateVH(binding, viewType)
            }
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutResource, parent, false)
        return groupManager.onCreateVH(view, viewType)
    }

    override fun onBindViewHolder(holder: BaseVH<*>, position: Int) {
//        Log.e("GROUP_DATA", "onBind: position")
        if (holder is GroupVH<*, *>) {
            groupManager.onBindViewHolder(holder as GroupVH<Any, GroupData<*>>, position)
        }
    }

    override fun onBindViewHolder(
        holder: BaseVH<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
//        Log.e("GROUP_DATA", "onBindViewHolder payload: position")
        if (payloads.isNotEmpty() && holder is GroupVH<*, *>) {
            groupManager.onBindViewHolder(holder as GroupVH<Any, GroupData<*>>, position, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return groupManager.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return groupManager.getItemViewType(position)
    }

    open fun hasBindingView() = false

    fun notifyAllGroupChanged() {
        groupManager.notifyDataSetChanged()
    }

    fun addGroupData(data: GroupData<*>) {
        groupManager.addGroupData(data)
    }

    fun addGroupDataAtIndex(index: Int, data: GroupData<*>) {
        groupManager.addGroupDataAtIndex(index, data)
    }

    fun removeGroup(data: GroupData<*>) {
        groupManager.removeGroup(data)
    }

    fun removeGroupWithoutNotify(data: GroupData<*>) {
        groupManager.removeGroupWithoutNotify(data)
    }

    fun clear() {
        groupManager.clear()
    }

    fun getPositionInGroup(adapterPosition: Int): Int {
        val group: GroupData<*>? = groupManager.findGroupDataByAdapterPosition(adapterPosition)
        return if (group != null) {
            adapterPosition - group.firstAdapterPositionGroup
        } else {
            -1
        }
    }

    fun getIndexOfGroup(data: GroupData<*>): Int {
        return groupManager.getIndexOfGroupData(data)
    }

    fun addRawGroupAtIndex(index: Int, groupData: GroupData<*>) {
        groupManager.addRawGroupDataAtIndex(index, groupData)
    }
}
