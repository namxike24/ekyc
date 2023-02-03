package ai.ftech.fekyc.base.adapter.group


import ai.ftech.fekyc.base.adapter.BaseVH
import android.view.View
import androidx.annotation.LayoutRes

abstract class GroupData<DATA>(var data: DATA) {
    companion object {
        const val INVALID_RESOURCE = -1
    }

    var firstAdapterPositionGroup = -1

    var groupManager: GroupManager? = null

    abstract fun getItemViewType(position: Int): Int

    abstract fun getDataInGroup(positionInGroup: Int): Any

    @LayoutRes
    abstract fun getLayoutResource(viewType: Int): Int

    abstract fun getCount(): Int

    abstract fun onCreateVH(itemView: View, viewType: Int): BaseVH<*>?

    open fun attach() {
        firstAdapterPositionGroup = groupManager?.findAdapterPositionForGroup(this) ?: -1
    }

    open fun detach() {
        firstAdapterPositionGroup = -1
    }

    open fun isAttached(): Boolean {
        return firstAdapterPositionGroup > -1
    }

    open fun notifyRemove(groupPosition: Int) {
        if (isAttached()) {
            groupManager?.notifyRemove(this, firstAdapterPositionGroup + groupPosition)
        }
    }

    open fun notifyRemove(groupPosition: Int, count: Int) {
        if (isAttached()) {
            groupManager?.notifyRemove(this, firstAdapterPositionGroup + groupPosition, count)
        }
    }

    open fun notifyInserted(groupPosition: Int, count: Int) {
        if (count <= 0) {
            return
        }

        if (!isAttached()) {
            attach()
        }
        groupManager?.notifyInserted(this, firstAdapterPositionGroup + groupPosition, count)
    }

    open fun notifyChanged(groupPosition: Int) {
        if (!isAttached()) {
            attach()
        }
        groupManager?.notifyChanged(this, firstAdapterPositionGroup + groupPosition)
    }

    open fun notifyChanged(groupPosition: Int, payload: Any?) {
        if (!isAttached()) {
            attach()
        }
        groupManager?.notifyChanged(this, firstAdapterPositionGroup + groupPosition, payload)
    }

    open fun notifyChanged() {
        groupManager?.notifyChanged(this)
    }

    open fun notifyDataSetChanged() {
        groupManager?.notifyDataSetChanged()
    }

    open fun notifyChange(payload: Any?) {
        groupManager?.notifyChanged(this, payload)
    }

    open fun notifySelfInserted() {
        groupManager?.notifyNewGroupAdded(this)
    }

    open fun notifySelfRemoved() {
        groupManager?.removeGroup(this)
    }

    open fun mapAdapterPositionToGroupPosition(adapterPosition: Int): Int {
        return adapterPosition - this.firstAdapterPositionGroup
    }

    open fun getAdapterPositionFromGroupPosition(groupPosition: Int): Int {
        return firstAdapterPositionGroup + groupPosition
    }
}
