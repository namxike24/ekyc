package ai.ftech.ekyc.common.widget.recyclerview

import ai.ftech.base.adapter.BaseAdapter
import ai.ftech.ekyc.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class CollectionView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : FrameLayout(ctx, attrs) {
    companion object {
        var numberPerPage: Int = 20 // số bản ghi muốn hiển thị trên 1 page
    }

    // View này dùng để hiển thị danh sách dữ liệu
    private lateinit var rvRecyclerView: RecyclerView

    // View này dùng để chứa các view không có dữ liệu
    private lateinit var flMessage: FrameLayout

    // View này dùng để chứa các view hiển thị trước khi initData vào
    private lateinit var flPreviewHint: FrameLayout

    // Sự kiện paging của RecyclerView
    private lateinit var endlessScrollListener: BaseRecyclerViewEndlessScrollListener

    // Sự kiện tải thêm dữ liệu
    private var loadMoreConsumer: (() -> Unit)? = null

    // Sự kiện khi không có dữ liệu
    private var emptyConsumer: (() -> Unit)? = null

    // layout manger
    private var layoutManagerMode: COLLECTION_MODE = COLLECTION_MODE.VERTICAL

    @Deprecated("Move to layoutManagerMode")
    lateinit var layoutManager: RecyclerView.LayoutManager

    // has fixed size
    private var hasFixedSize: Boolean = true

    private var baseAdapter: BaseAdapter? = null

    private var visibleItem: Int? = null

    private var visibleDecorationSizeItem: Int? = null

    private var maxVisibleHeight: Int? = null

    val adapter: BaseAdapter
        get() {
            if (baseAdapter == null) throw NullPointerException()
            return baseAdapter as BaseAdapter
        }

    val itemCount: Int
        get() {
            if (baseAdapter == null) return 0
            return items.size
        }

    var items: MutableList<Any>
        get() {
            if (baseAdapter == null) throw NullPointerException()

            return baseAdapter!!.dataList
        }
        set(its) {
            if (baseAdapter == null) throw NullPointerException()
            baseAdapter!!.resetData(its)
            displayMessage()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.fekyc_collection_view_layout, this, true)
        initView(ctx, attrs)
    }

    fun setAdapter(adapter: BaseAdapter) {
        this.baseAdapter = adapter
        rvRecyclerView.adapter = adapter
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        this.rvRecyclerView.addItemDecoration(itemDecoration)
    }

    fun setLayoutManager(mode: COLLECTION_MODE, gridNumber: Int = 1) {
        this.layoutManagerMode = mode
        val layoutManager = when (mode) {
            COLLECTION_MODE.VERTICAL -> LinearLayoutManager(context)
            COLLECTION_MODE.HORIZONTAL -> LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            COLLECTION_MODE.GRID -> GridLayoutManager(context, gridNumber)
            COLLECTION_MODE.STAGGERED_GRID_VERTICAL -> StaggeredGridLayoutManager(gridNumber, StaggeredGridLayoutManager.VERTICAL)
            COLLECTION_MODE.STAGGERED_GRID_HORIZONTAL -> StaggeredGridLayoutManager(gridNumber, StaggeredGridLayoutManager.HORIZONTAL)
        }
        rvRecyclerView.layoutManager = layoutManager
    }

    @JvmName("setLayoutManagerDeprecated")
    @Deprecated("Move to layoutManagerMode")
    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        this.layoutManager = layoutManager
    }

    fun setCustomizeViewPreviewHint(@LayoutRes layoutId: Int, onViewAdded: ((View) -> Unit)? = null) {
        val view = LayoutInflater.from(context).inflate(layoutId, null).apply {
            onViewAdded?.invoke(this)
        }
        this.flPreviewHint.removeAllViews()
        this.flPreviewHint.addView(view)

        flPreviewHint.visibility = View.VISIBLE
    }

    fun setCustomizeViewNoData(@LayoutRes layoutId: Int, onViewAdded: ((View) -> Unit)? = null) {
        val view = LayoutInflater.from(context).inflate(layoutId, null).apply {
            onViewAdded?.invoke(this)
        }
        this.flMessage.removeAllViews()
        this.flMessage.addView(view)
    }

    fun setLoadMoreListener(loadMoreConsumer: (() -> Unit)? = null) {
        this.loadMoreConsumer = loadMoreConsumer
    }

    fun setEmptyListener(emptyConsumer: (() -> Unit)? = null) {
        this.emptyConsumer = emptyConsumer
    }

    fun resetData(items: List<Any>) {
        clearData()
        addItems(items)
    }

    fun clearData() {
        if (baseAdapter == null) throw NullPointerException()
        flMessage.visibility = View.GONE
        flPreviewHint.visibility = View.GONE
        endlessScrollListener.resetState()
        baseAdapter?.clearData()
    }

    fun getItem(position: Int): Any? {
        return baseAdapter?.getDataAtPosition(position)
    }

    fun addItem(item: Any) {
        baseAdapter?.add(item)
    }

    fun addItem(index: Int, item: Any) {
        baseAdapter?.add(item, index)
    }

    fun addItems(items: List<Any>) {
        if (items.isNotEmpty() && items.size < numberPerPage) {
            endlessScrollListener.lastPage = true
        }

        // Thêm vào danh sách
        baseAdapter?.addListItem(items)

        if (visibleItem != null && maxVisibleHeight == null) {
            rvRecyclerView.post {
                val item = rvRecyclerView.findViewHolderForAdapterPosition(0)
                maxVisibleHeight = item?.itemView?.height?.times(visibleItem!!)?.toInt()

                //tính chiều cao của item decoration nếu có
                if (visibleDecorationSizeItem != null && item?.itemView != null) {
                    maxVisibleHeight = maxVisibleHeight?.plus(rvRecyclerView.layoutManager?.getTopDecorationHeight(item.itemView)?.times(visibleDecorationSizeItem!!)
                        ?: 0)
                }

                //set lại height collectionView
                maxVisibleHeight?.let {
                    rvRecyclerView.updateLayoutParams {
                        height = it
                    }
                }
            }
        }


        // Kiểm tra hiển thị không có dữ liệu
        displayMessage()
    }

    fun update(item: Any, position: Int) {
        baseAdapter?.update(item, position)
    }

    fun removeItem(item: Any, position: Int) {
        baseAdapter?.removeItem(item, position)

        // Kiểm tra hiển thị không có dữ liệu
        displayMessage()
    }

    fun removeItem(position: Int) {
        if (position != -1) baseAdapter?.removeItem(position)

        // Kiểm tra hiển thị không có dữ liệu
        displayMessage()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() {
        baseAdapter?.notifyDataSetChanged()

        // Kiểm tra hiển thị không có dữ liệu
        displayMessage()
    }

    fun notifyItem(position: Int) {
        baseAdapter?.notifyItemChanged(position)
    }

    fun scrollToPosition(position: Int) {
        if (position > -1 && position < itemCount) {
            rvRecyclerView.smoothScrollToPosition(position)
        }
    }

    fun displayMessage() {
        val count = baseAdapter?.dataList?.filterNotNull()?.size ?: 0
//        flMessage.visibility = if (count <= 0 && !shimmerFrameLayout.isShimmerStarted) View.VISIBLE else View.GONE
        flMessage.visibility = if (count <= 0) View.VISIBLE else View.GONE
        flPreviewHint.visibility = View.GONE
        // Gọi sự kiện ra bên ngoài khi không có dữ liệu
        if (count <= 0) {
            emptyConsumer?.invoke()
        }
    }

    //Decoration size sẽ có thể bằng limitSize hoặc  limitSize +1/-1 tùy thuộc vào thuộc tính IgnoreTop, ignoreBottom của ItemDecoration
    fun setMaximumVisibleItem(limitSize: Int?, decorationSize: Int? = null) {
        this.visibleItem = limitSize
        this.visibleDecorationSizeItem = decorationSize
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        // default layout manager
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // rvRecyclerView
        rvRecyclerView = findViewById(R.id.rvRecyclerView)

        //set default layout manager
        rvRecyclerView.layoutManager = LinearLayoutManager(context)

        // No data
        flMessage = findViewById(R.id.flMessage)
        flMessage.visibility = View.GONE

        // Preview hint
        flPreviewHint = findViewById(R.id.flPreviewHint)
        flPreviewHint.visibility = View.GONE

        // number per page
        if (numberPerPage != 0) rvRecyclerView.setItemViewCacheSize(numberPerPage)

        // adapter
        rvRecyclerView.setHasFixedSize(hasFixedSize)
        rvRecyclerView.isNestedScrollingEnabled = true

        // update load more
        endlessScrollListener = object : BaseRecyclerViewEndlessScrollListener(rvRecyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int) {
                loadMoreConsumer?.invoke()
            }
        }

        rvRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    fun build() {
        if (baseAdapter == null) throw NullPointerException()

        // number per page
        if (numberPerPage != 0) rvRecyclerView.setItemViewCacheSize(numberPerPage)

        // adapter
        rvRecyclerView.layoutManager = layoutManager
        rvRecyclerView.setHasFixedSize(hasFixedSize)
        rvRecyclerView.adapter = baseAdapter
        rvRecyclerView.isNestedScrollingEnabled = true

        // update load more
        endlessScrollListener = object : BaseRecyclerViewEndlessScrollListener(rvRecyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int) {
                loadMoreConsumer?.invoke()
            }
        }

        rvRecyclerView.addOnScrollListener(endlessScrollListener)
    }

    enum class COLLECTION_MODE {
        VERTICAL, HORIZONTAL, GRID, STAGGERED_GRID_VERTICAL, STAGGERED_GRID_HORIZONTAL
    }
}
