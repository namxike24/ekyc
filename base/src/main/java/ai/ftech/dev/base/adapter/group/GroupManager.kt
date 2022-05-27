package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseVH
import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GroupManager(
    private val adapter: RecyclerView.Adapter<*>
) {

    private val groupDataList = mutableListOf<GroupData<*>>()

    fun onCreateVH(itemViewBinding: ViewDataBinding, viewType: Int): BaseVH<*> {
        groupDataList.forEach {
            val vh: BaseVH<*>? = it.onCreateVH(itemViewBinding, viewType)
            if (vh != null) {
                return vh
            }
        }
        throw IllegalArgumentException("Can not find ViewHolder for type: $viewType")
    }

    fun onBindViewHolder(vh: GroupVH<Any, GroupData<*>>, position: Int) {
        vh.groupData = findGroupDataByAdapterPosition(position)
        vh.onBind(getItemDataAtAdapterPosition(position))
    }

    fun onBindViewHolder(
        vh: GroupVH<Any, GroupData<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(vh, position)
        } else {
            vh.groupData = findGroupDataByAdapterPosition(position)
            vh.onBind(getItemDataAtAdapterPosition(position), payloads)
        }
    }

    fun getData(): List<GroupData<*>> = groupDataList

    fun addRawGroupDataAtIndex(index: Int, data: GroupData<*>) {
        data.groupManager = this
        groupDataList.add(index, data)
    }

    fun addGroupData(data: GroupData<*>) {
        data.groupManager = this
        groupDataList.add(data)
        if (data.getCount() == 0) {
            data.detach()
        } else if (!data.isAttached()) {
            data.attach()
        }
    }

    fun addGroupDataAtIndex(index: Int, data: GroupData<*>) {
        data.groupManager = this
        groupDataList.add(index, data)
        if (data.getCount() == 0) {
            data.detach()
        } else if (!data.isAttached()) {
            data.attach()
            shiftAdapterPosition(data, data.getCount())
        }
    }

    fun removeGroup(groupData: GroupData<*>) {
        if (groupDataList.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount())
            val firstAdapterPositionGroup = groupData.firstAdapterPositionGroup
            groupData.detach()
            groupDataList.remove(groupData)
            adapter.notifyItemRangeRemoved(firstAdapterPositionGroup, groupData.getCount())
        }
    }

    fun removeGroupWithoutNotify(groupData: GroupData<*>) {
        if (groupDataList.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount())
            groupData.detach()
            groupDataList.remove(groupData)
        }
    }

    fun getItemCount(): Int {
        var total = 0
        groupDataList.forEach {
            if (it.isAttached()) {
                total += it.getCount()
            }
        }
        return total
    }

    fun getLayoutResource(viewType: Int): Int {
        groupDataList.forEach {
            val layoutResource: Int = it.getLayoutResource(viewType)
            if (layoutResource != GroupData.INVALID_RESOURCE) {
                return layoutResource
            }
        }
        throw IllegalArgumentException("Can not find layout for type: $viewType")
    }

    fun getItemViewType(adapterPosition: Int): Int {
        val data: GroupData<*>? = findGroupDataByAdapterPosition(adapterPosition)
        return data?.getItemViewType(adapterPosition - data.firstAdapterPositionGroup)
            ?: throw IllegalArgumentException("Can not find data for position: $adapterPosition")
    }


    fun getItemDataAtAdapterPosition(adapterPosition: Int): Any {
        val data: GroupData<*>? = findGroupDataByAdapterPosition(adapterPosition)
        return data?.getDataInGroup(adapterPosition - data.firstAdapterPositionGroup)
            ?: throw IllegalArgumentException("Can not find data for position: $adapterPosition")
    }

    fun notifyRemove(group: GroupData<*>, adapterPosition: Int) {
        shiftAdapterPosition(group, -1)
        adapter.notifyItemRemoved(adapterPosition)
        checkToDetach(group)
    }

    fun notifyRemove(group: GroupData<*>, adapterPosition: Int, count: Int) {
        shiftAdapterPosition(group, -count)
        adapter.notifyItemRangeRemoved(adapterPosition, count)
        checkToDetach(group)
    }

    fun notifyInserted(group: GroupData<*>, adapterPosition: Int, count: Int) {
        shiftAdapterPosition(group, count)
        adapter.notifyItemRangeInserted(adapterPosition, count)
    }

    fun notifyChanged(group: GroupData<*>, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition)
    }

    fun notifyChanged(group: GroupData<*>, adapterPosition: Int, payload: Any?) {
        adapter.notifyItemChanged(adapterPosition, payload)
    }

    fun notifyChanged(group: GroupData<*>) {
        adapter.notifyItemRangeChanged(group.firstAdapterPositionGroup, group.getCount())
    }

    fun notifyChanged(group: GroupData<*>, payload: Any?) {
        adapter.notifyItemRangeChanged(group.firstAdapterPositionGroup, group.getCount(), payload)
    }

    fun notifyNewGroupAdded(group: GroupData<*>) {
        if (!group.isAttached()) {
            group.attach()
            shiftAdapterPosition(group, group.getCount())
        }
        adapter.notifyItemRangeInserted(group.firstAdapterPositionGroup, group.getCount())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() {
        groupDataList.forEach {
            if (it.isAttached() && it.getCount() <= 0) {
                it.detach()
            } else {
                if (it.getCount() > 0) {
                    it.attach()
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun findGroupDataByAdapterPosition(adapterPosition: Int): GroupData<*>? {
        groupDataList.forEach {
            if (it.isAttached()) {
                if (it.firstAdapterPositionGroup <= adapterPosition
                    && adapterPosition < it.firstAdapterPositionGroup + it.getCount()
                ) {
                    return it
                }
            }
        }
        return null
    }

    fun findAdapterPositionForGroup(group: GroupData<*>?): Int {
        val index: Int = groupDataList.indexOf(group)
        return when {
            index > 0 -> {
                for (i in index - 1 downTo 0) {
                    val prev: GroupData<*> = groupDataList[i]
                    if (prev.isAttached()) {
                        return prev.firstAdapterPositionGroup + prev.getCount()
                    }
                }
                0
            }
            index == 0 -> {
                0
            }
            else -> {
                throw IllegalArgumentException("The GroupData is not added")
            }
        }
    }

    fun getIndexOfGroupData(groupData: GroupData<*>?): Int {
        return if (groupData == null) {
            -1
        } else groupDataList.indexOf(groupData)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        val itr: MutableIterator<GroupData<*>> = groupDataList.iterator()
        while (itr.hasNext()) {
            val next: GroupData<*> = itr.next()
            shiftAdapterPosition(next, -next.getCount())
            next.detach()
            itr.remove()
        }
        adapter.notifyDataSetChanged()
    }

    private fun shiftAdapterPosition(startGroup: GroupData<*>, count: Int) {
        val startIndex: Int = groupDataList.indexOf(startGroup)
        for (i in startIndex + 1 until groupDataList.size) {
            val next: GroupData<*> = groupDataList[i]
            if (next.isAttached()) {
                next.firstAdapterPositionGroup += count
            }
        }
    }

    private fun checkToDetach(group: GroupData<*>) {
        if (group.getCount() == 0) {
            group.detach()
        }
    }
}
