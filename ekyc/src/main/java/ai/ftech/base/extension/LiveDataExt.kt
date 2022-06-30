package ai.ftech.base.extension

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    }
    )
}

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

fun <E> LiveData<List<E>>.isEmptyList(): Boolean {
    return value.isNullOrEmpty()
}

fun <T> MutableLiveData<T>.postSelf() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.setSelf() {
    value = this.value
}

inline fun <reified E> MutableLiveData<E>.postIfChanged(newValue: E) {
    if (this.value != newValue) {
        postValue(newValue)
    }
}

fun <E> MutableLiveData<E>.setIfChanged(newValue: E) {
    if (this.value != newValue) {
        value = this.value
    }
}

fun MutableLiveData<Boolean>.postReverseBoolean() {
    val currentValue = value ?: false
    postValue(!currentValue)
}

fun MutableLiveData<Boolean>.setReverseBoolean() {
    val currentValue = value ?: false
    value = !currentValue
}

class SingleLiveEvent<T> constructor() : MutableLiveData<T>() {

    private val TAG = SingleLiveEvent::class.simpleName
    private var isPending = AtomicBoolean(false)

    constructor(t: T) : this() {
        postValue(t)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.e(TAG, "Multiple observers registered but only one will be notified of changes")
        }
        super.observe(owner) {
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(value: T?) {
        isPending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }
}
