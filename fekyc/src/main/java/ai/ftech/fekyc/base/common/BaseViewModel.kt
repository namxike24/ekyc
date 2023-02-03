package ai.ftech.fekyc.base.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModel : ViewModel()

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application)

class ViewModelFactory(private vararg val params: Any) : ViewModelProvider.NewInstanceFactory() {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        val listClazz = params.map { clazz -> clazz.javaClass }.toTypedArray()
//        val constructor = modelClass.getDeclaredConstructor(*listClazz)
//        return constructor.newInstance(*params)
//    }
}
