package ai.ftech.dev.base.common

import ai.ftech.dev.base.common.navigation.FragmentAnim
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes private val layoutId: Int) : Fragment(), BaseView {
    protected val TAG = this::class.java.simpleName
    protected val binding get() = _binding!!
    private var _binding: DB? = null
    private val baseActivity by lazy {
        requireActivity() as BaseActivity<*>
    }
    private lateinit var myInflater: LayoutInflater
    private lateinit var callback: OnBackPressedCallback

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(requireActivity())
        }
        _binding =
            DataBindingUtil.inflate(myInflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onInitBinding()
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (isHandleBackPressByFragment()) {
            setBackPressedDispatcher()
        }
        setHasOptionsMenu(isAttachMenuToFragment())
        onInitView()
        onObserverViewModel()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onCleaned()
    }

    override fun onPrepareInitView() {
        setupStatusBar().let {
            setStatusColor(it.color, it.isDarkText)
        }
    }

    override fun onInitView() {
    }

    override fun onCleaned() {
        super.onCleaned()
    }

    open fun isAttachMenuToFragment(): Boolean = true

    open fun isHandleBackPressByFragment(): Boolean = true

    open fun getContainerId(): Int = LAYOUT_INVALID

    open fun setupStatusBar(): StatusBar {
        return baseActivity.setupStatusBar()
    }

    open fun isSoftInputAdjustResize(): Boolean {
        return baseActivity.isSoftInputAdjustResize()
    }

    open fun onBackPressedFragment(tag: String? = null) {
        backScreenByFragmentManager(tag)
    }

    //region navigate screen
    fun navigateTo(clazz: Class<out BaseActivity<*>>, onCallback: (Intent) -> Unit = {}) {
        baseActivity.navigateTo(clazz, onCallback)
    }

    fun navigateTo(clazz: Class<out BaseActivity<*>>, bundle: Bundle, onCallback: (Intent) -> Unit = {}) {
        baseActivity.navigateTo(clazz, bundle, onCallback)
    }

    fun navigateTo(clazz: Class<out BaseActivity<*>>, fragmentClass: Class<out BaseFragment<*>>, onCallback: (Intent) -> Unit = {}) {
        baseActivity.navigateTo(clazz, fragmentClass, onCallback)
    }

    fun navigateTo(clazz: Class<out BaseActivity<*>>, fragmentClass: Class<out BaseFragment<*>>,  bundle: Bundle, onCallback: (Intent) -> Unit = {}) {
        baseActivity.navigateTo(clazz, fragmentClass, bundle, onCallback)
    }

    fun navigateBack() {
        baseActivity.navigateBack()
    }

    fun replaceFragment(
        fragment: BaseFragment<*>,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        fragmentAnim: FragmentAnim = FragmentAnim()
    ) {
        baseActivity.replaceFragment(fragment, bundle, keepToBackStack, fragmentAnim)
    }

    fun addFragment(
        fragment: BaseFragment<*>,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        fragmentAnim: FragmentAnim = FragmentAnim()
    ) {
        baseActivity.addFragment(fragment, bundle, keepToBackStack, fragmentAnim)
    }

    fun backFragment() {
        baseActivity.backFragment()
    }

    fun backFragment(tag: String) {
        baseActivity.backFragment(tag)
    }

    fun clearStackFragment() {
        baseActivity.clearStackFragment()
    }

    fun getCurrentFragment(): Fragment? {
        return baseActivity.getCurrentFragment()
    }

    fun replaceFragmentInsideFragment(
        fragment: BaseFragment<*>,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        fragmentAnim: FragmentAnim = FragmentAnim()
    ) {
        includeFragment(
            fragment,
            bundle,
            getContainerId(),
            true,
            keepToBackStack,
            fragmentAnim
        )
    }

    fun addFragmentInsideFragment(
        fragment: BaseFragment<*>,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        fragmentAnim: FragmentAnim = FragmentAnim()
    ) {
        includeFragment(
            fragment,
            bundle,
            getContainerId(),
            false,
            keepToBackStack,
            fragmentAnim
        )
    }
    //endregion

    fun setStatusColor(color: Int = android.R.color.black, isDarkText: Boolean = true) {
        baseActivity.setStatusColor(color, isDarkText)
    }

    fun setFullScreen() {
        baseActivity.setFullScreen()
    }

//    fun setFullScreen() {
//        baseActivity.setFullScreen()
//    }

    private fun backScreenByFragmentManager(tag: String? = null) {
        if (tag != null) {
            baseActivity.backFragment(tag)
        } else {
            baseActivity.backFragment()
        }
    }

    private fun setBackPressedDispatcher() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    //region fragment backstack
    private fun includeFragment(
        fragment: Fragment,
        bundle: Bundle?,
        containerId: Int,
        isReplace: Boolean,
        keepToBackStack: Boolean,
        fragmentAnim: FragmentAnim
    ) {
        if (getContainerId() == LAYOUT_INVALID) {
            throw IllegalArgumentException("Cần phải gán container id để replace fragment")
        }
        try {
            val tag = fragment::class.java.simpleName
            bundle?.let {
                fragment.arguments = it
            }
            childFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    fragmentAnim.enter,
                    fragmentAnim.exist,
                    fragmentAnim.popEnter,
                    fragmentAnim.popExit
                )
                if (isReplace) {
                    replace(containerId, fragment, tag)
                } else {
                    add(containerId, fragment, tag)
                }
                if (keepToBackStack) {
                    addToBackStack(tag)
                }
                commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //endregion
}
