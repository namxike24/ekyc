package ai.ftech.dev.base.common

import ai.ftech.dev.base.common.navigation.FragmentAnim
import ai.ftech.dev.base.extension.getAppColor
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


abstract class BaseActivity<DB : ViewDataBinding>(@LayoutRes private val layoutId: Int) : AppCompatActivity(), BaseView {

    interface PermissionListener {
        fun onAllow()
        fun onDenied()
        fun onNeverAskAgain()
    }

    companion object {
        const val FRAGMENT_NAME = "FRAGMENT_NAME"
        const val FRAGMENT_BUNDLE = "FRAGMENT_BUNDLE"
    }

    var isFullScreen = false
        private set
    protected val TAG = this::class.java.simpleName
    protected val binding
        get() = _binding!!
    private var _binding: DB? = null
    private var permissionListener: PermissionListener? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                map.forEach { (k, v) ->
                    if (v) {
                        permissionListener?.onAllow()
                    } else {
                        if (!shouldShowRequestPermissionRationale(k)) {
                            permissionListener?.onNeverAskAgain()
                            return@registerForActivityResult
                        }
                    }
                    permissionListener?.onDenied()
                }
            }
        }
    private var safeAction = false
    private var waitingAction: (() -> Unit)? = null
    private var keyboardGlobalListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    init {

    }

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        onPrepareInitView()
        if (isOnlyPortraitScreen()) {
            setPortraitScreen()
        }
        super.onCreate(savedInstanceState)
        try {
            if (isFixSingleTask()) {
                if (!isTaskRoot) {
                    finish()
                    return
                }
            }
            _binding = DataBindingUtil.setContentView(this, layoutId)
            binding.lifecycleOwner = this
            onInitBinding()
            onInitView()
            onObserverViewModel()
        } catch (e: InflateException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        safeAction = true
        if (waitingAction != null) {
            waitingAction?.invoke()
            waitingAction = null
        }
    }

    override fun onPause() {
        safeAction = false
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onInitView() {
        setupStatusBar().let {
            setStatusColor(it.color, it.isDarkText)
        }

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
    //endregion

    open fun isFixSingleTask(): Boolean = false

    open fun isOnlyPortraitScreen(): Boolean = false

    open fun getContainerId(): Int = LAYOUT_INVALID

    open fun isSoftInputAdjustResize(): Boolean = false

    open fun setupStatusBar(): StatusBar = StatusBar()

    //region navigate screen
    fun navigateTo(clazz: Class<out BaseActivity<*>>, onCallback: (Intent) -> Unit = {}) {
        val intent = Intent(this, clazz)
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun navigateTo(
        clazz: Class<out BaseActivity<*>>,
        bundle: Bundle,
        onCallback: (Intent) -> Unit = {}
    ) {
        val intent = Intent(this, clazz)
        intent.putExtras(bundle)
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun navigateTo(
        clazz: Class<out BaseActivity<*>>,
        fragmentClazz: Class<out BaseFragment<*>>,
        onCallback: (Intent) -> Unit = {}
    ) {
        val intent = Intent(this, clazz)
        intent.putExtra(FRAGMENT_NAME, fragmentClazz.name)
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun navigateTo(
        clazz: Class<out BaseActivity<*>>,
        fragmentClazz: Class<out BaseFragment<*>>,
        bundle: Bundle,
        onCallback: (Intent) -> Unit = {}
    ) {
        val intent = Intent(this, clazz)
        intent.putExtra(FRAGMENT_NAME, fragmentClazz.name)
        intent.putExtra(FRAGMENT_BUNDLE, bundle)
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun navigateBack() {
        onBackPressed()
    }

    fun replaceFragment(
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

    fun addFragment(
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

    fun backFragment() {
        supportFragmentManager.popBackStack()
    }

    fun backFragment(tag: String) {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun clearStackFragment() {
        supportFragmentManager.let { fm ->
            fm.backStackEntryCount.let { count ->
                for (i in 0..count) {
                    fm.popBackStack()
                }
            }
        }
    }

    fun getCurrentFragment(): Fragment? {
        val fragmentList = supportFragmentManager.fragments
        return fragmentList.lastOrNull()
    }
    //endregion

    //region status bar
    fun setStatusColor(color: Int = android.R.color.black, isDarkText: Boolean = true) {
        window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                decorView.let {
                    ViewCompat.getWindowInsetsController(it)?.apply {
                        // Light text == dark status bar
                        // máy ảo bị bug có lúc hiển thị sai cái này, trên device thật vẫn sẽ show bt
                        isAppearanceLightStatusBars = isDarkText
                    }
                }
            } else {
                //set text status old api
                decorView.let {
                    it.systemUiVisibility =
                        if (!isDarkText) {
                            it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        } else {
                            it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }
                }
            }

            //set status color
            statusBarColor = getAppColor(color)
        }
    }

    fun setFullScreen() {
        window?.apply {
            WindowCompat.setDecorFitsSystemWindows(this, false)
            isFullScreen = true
        }
    }

    fun setNormalScreen() {
        window?.apply {
            WindowCompat.setDecorFitsSystemWindows(this, true)
            isFullScreen = false
            //reset view
            (binding.root.layoutParams as MarginLayoutParams).setMargins(0, 0, 0, 0)
            binding.root.requestLayout()
        }
    }
    //endregion

    //region safe action
    fun doSafeAction(action: () -> Unit) {
        if (safeAction) {
            action.invoke()
        } else {
            waitingAction = action
        }
    }
    //endregion

    //region handle keyboard
    fun setKeyboardVisibilityListener(parent: View, listener: (Boolean) -> Unit) {
        if (keyboardGlobalListener != null) {
            removeKeyboardVisibilityListener(parent)
        }
        keyboardGlobalListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var wasShown: Boolean? = null

            override fun onGlobalLayout() {
                val defaultKeyboardHeightDP = 148
                val rect = Rect()
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    defaultKeyboardHeightDP.toFloat(),
                    parent.resources.displayMetrics
                ).toInt()
                parent.getWindowVisibleDisplayFrame(rect)
                val heightDiff = parent.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (wasShown == isShown) {
                    return
                } else {
                    wasShown = isShown
                    listener.invoke(isShown)
                }
            }
        }
        parent.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalListener)
    }

    fun removeKeyboardVisibilityListener(parent: View) {
        if (keyboardGlobalListener != null) {
            parent.viewTreeObserver.removeOnGlobalLayoutListener(keyboardGlobalListener)
            keyboardGlobalListener = null
        }
    }
    //endregion

    //region request permision
    fun doRequestPermission(
        permissions: Array<String>,
        listener: PermissionListener
    ) {
        this.permissionListener = listener
        if (checkPermission(permissions)) {
            this.permissionListener?.onAllow()
        } else {
            launcher.launch(permissions)
        }
    }

    private fun checkPermission(permissions: Array<String>): Boolean {
        permissions.forEach {
            if (checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }
    //endregion

    //region orientation
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitScreen() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }
    //endregion

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
            supportFragmentManager.beginTransaction().apply {
                /*setCustomAnimations(
                    fragmentAnim.enter,
                    fragmentAnim.exist,
                    fragmentAnim.popEnter,
                    fragmentAnim.popExit
                )*/
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
