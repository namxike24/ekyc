package ai.ftech.dev.base.common.binding

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.common.BaseView
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<DB : ViewDataBinding>(layoutId: Int) : BaseActivity(layoutId), BaseView {

    protected val binding
        get() = _binding!!
    private var _binding: DB? = null

    init {

    }

    //region lifecycle
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onInitView() {
        super.onInitView()
        //Tự tính toán lại khoảng cách view root khi set full màn
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v: View, windowInsets: WindowInsetsCompat ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val mlp = v.layoutParams as ViewGroup.MarginLayoutParams
            mlp.leftMargin = insets.left
            mlp.bottomMargin = insets.bottom
            mlp.rightMargin = insets.right
            v.layoutParams = mlp
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun attachView() {
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }
    //endregion
}
