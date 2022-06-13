package ai.ftech.dev.base.common.binding

import ai.ftech.dev.base.common.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<DB : ViewDataBinding>(layoutId: Int) : BaseFragment(layoutId) {
    protected val binding get() = _binding!!
    private var _binding: DB? = null

    override fun attachView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(myInflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}
