package ai.ftech.dev.base.cache

interface ICache<String, T> {
    fun put(key: String, data: T)
    operator fun get(key: String): T
    fun remove(key: String)
    fun update(key: String, data: T)
    fun removeAll()
    fun trimToSize(size: Int)
    val size: Int

    fun sizeOf(key: String, item: T): Int
    val allValues: Collection<T>?
    val allKeys: Set<String>?
}
