package ai.ftech.dev.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter : RecyclerView.Adapter<BaseVH<Any>>() {

    companion object {
        const val INVALID_RESOURCE = -1
    }

    var dataList: MutableList<Any> = mutableListOf()
        private set

    private lateinit var myInflater: LayoutInflater

    abstract fun getLayoutResource(viewType: Int): Int

    abstract fun onCreateViewHolder(viewType: Int, view: View): BaseVH<*>

    abstract fun getDataAtPosition(position: Int): Any

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<Any> {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(parent.context)
        }
        val view = myInflater.inflate(getLayoutResource(viewType), parent, false)
        return onCreateViewHolder(viewType, view) as BaseVH<Any>
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BaseVH<Any>, position: Int) {
        holder.onBind(getDataAtPosition(position))
    }

    override fun onBindViewHolder(holder: BaseVH<Any>, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.onBind(getDataAtPosition(position), payloads)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun resetData(dataList: List<Any>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun addListItem(dataList: List<Any>) {
        val size = this.dataList.size
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(size, dataList.size)
    }

    fun add(item: Any) {
        this.dataList.add(item)
        notifyItemInserted(dataList.size)
    }

    fun add(item: Any, position: Int) {
        this.dataList.add(item)
        notifyItemInserted(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        this.dataList.clear()
        notifyDataSetChanged()
    }

    fun update(item: Any, position: Int) {
        dataList[position] = item
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        if(dataList.isNotEmpty()){
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItem(item: Any, position: Int){
        if(dataList.isNotEmpty()){
            dataList.remove(item)
            notifyItemRemoved(position)
        }
    }
}
