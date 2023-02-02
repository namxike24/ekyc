package ai.ftech.ekyc.base.cache

import android.util.LruCache

class LruCacheImpl<T>(maxSize: Int) : ICache<String?, T> {

    private val mCache: LruCache<String, T> = LruCache(maxSize)

    override fun put(key: String?, data: T) {
        mCache.put(key, data)
    }

    override fun get(key: String?): T {
        return mCache[key]
    }

    override fun remove(key: String?) {
        mCache.remove(key)
    }

    override fun update(key: String?, data: T) {
        mCache.remove(key)
        mCache.put(key, data)
    }

    override fun removeAll() {
        mCache.evictAll()
    }

    override fun trimToSize(size: Int) {
        mCache.trimToSize(size)
    }

    override val size: Int
        get() = mCache.size()

    override fun sizeOf(key: String?, item: T): Int {
        return 1
    }

    override val allValues: Collection<T>
        get() = mCache.snapshot().values

    override val allKeys: Set<String>
        get() = mCache.snapshot().keys

}
