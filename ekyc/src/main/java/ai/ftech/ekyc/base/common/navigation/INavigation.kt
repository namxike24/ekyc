package ai.ftech.ekyc.base.common.navigation

import ai.ftech.ekyc.base.common.BaseActivity
import ai.ftech.ekyc.base.common.BaseFragment
import android.os.Bundle
import androidx.fragment.app.Fragment

interface INavigation

interface INavByActivity : INavigation {
    fun navigateTo(clazz: Class<out BaseActivity>)
    fun navigateTo(clazz: Class<out BaseActivity>, bundle: Bundle)
    fun navigateBack()
}

interface INavByFragment : INavigation {
    fun replaceFragment(
        fragment: BaseFragment,
        bundle: Bundle?,
        keepToBackStack: Boolean,
        fragmentAnim: FragmentAnim
    )

    fun addFragment(
        fragment: BaseFragment,
        bundle: Bundle?,
        keepToBackStack: Boolean,
        fragmentAnim: FragmentAnim
    )

    fun backFragment()
    fun backFragment(tag: String)
    fun clearStackFragment()
    fun getCurrentFragment(): Fragment?
}
