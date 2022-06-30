package ai.ftech.base.cache

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.util.LruCache

class CacheManager : ComponentCallbacks2 {

    private val cacheStore: LruCache<String, ICache<String, *>?> =
        LruCache<String, ICache<String, *>?>(Int.MAX_VALUE)

    override fun onTrimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            val snapshot: Map<String, ICache<String, *>?> = cacheStore.snapshot()
            for (id in snapshot.keys) {
                val cache: ICache<String, *>? = cacheStore[id]
                cache?.removeAll()
            }
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            val snapshot: Map<String, ICache<String, *>?> = cacheStore.snapshot()
            for (id in snapshot.keys) {
                val cache: ICache<String, *>? = cacheStore[id]
                cache?.trimToSize(cache.size / 2)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}

    override fun onLowMemory() {}

    fun hasCache(cacheName: String): Boolean {
        return cacheStore[cacheName] != null
    }

    fun <T> createCache(
        cacheName: String,
        maxSize: Int,
        factory: ICacheFactory<T>
    ): ICache<String, T>? {
        if (hasCache(cacheName)) {
            return cacheStore[cacheName] as ICache<String, T>?
        }
        val cache: ICache<String, T> = factory.createCache(cacheName, maxSize) as ICache<String, T>
        cacheStore.put(cacheName, cache)
        return cache
    }

    fun getCache(cacheName: String): ICache<String, *>? {
        return cacheStore[cacheName]
    }

    companion object {
        private var sInstance: CacheManager? = null
        val instance: CacheManager?
            get() {
                if (sInstance == null) {
                    synchronized(CacheManager::class.java) {
                        if (sInstance == null) {
                            sInstance = CacheManager()
                        }
                    }
                }
                return sInstance
            }
    }
}
