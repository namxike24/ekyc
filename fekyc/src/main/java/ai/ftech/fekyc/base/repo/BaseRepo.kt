package ai.ftech.fekyc.base.repo

import ai.ftech.fekyc.base.cache.CacheManager
import ai.ftech.fekyc.base.cache.DefaultCacheFactory
import ai.ftech.fekyc.base.cache.ICache
import android.util.Log

open class BaseRepo : IRepo {
    fun <T> buildCache(cacheName: String): CacheBuilder<*> {
        return CacheBuilder<T>().name(cacheName)
    }

    fun <T> getCache(cacheName: String): ICache<String, T> {
        return CacheManager.instance?.getCache(cacheName) as ICache<String, T>
    }

    class CacheBuilder<T> {
        private var name: String? = null
        private var maxSize = Int.MAX_VALUE
        fun name(cacheName: String?): CacheBuilder<*> {
            name = cacheName
            return this
        }

        fun maxSize(maxSize: Int): CacheBuilder<*> {
            this.maxSize = maxSize
            return this
        }

        fun build(): ICache<String, T>? {
            val manager: CacheManager = CacheManager.instance!!
            if (manager.hasCache(name!!)) {
                Log.e("BaseRepo", "has cache with name: $name")
            } else {
                return manager.createCache(name!!, maxSize, DefaultCacheFactory())
            }
            return null
        }
    }
}
