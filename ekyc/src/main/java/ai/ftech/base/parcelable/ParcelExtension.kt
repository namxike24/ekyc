package ai.ftech.base.parcelable

import android.os.Parcel
import android.os.Parcelable

inline fun <reified T> parcelableCreator(crossinline create: (Parcel) -> T) =
    object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel) = create(source)
        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }

inline fun <reified T> parcelableClassLoaderCreator(crossinline create: (Parcel, ClassLoader) -> T) =
    object : Parcelable.ClassLoaderCreator<T> {
        override fun createFromParcel(source: Parcel, loader: ClassLoader) = create(source, loader)

        override fun createFromParcel(source: Parcel) =
            createFromParcel(source, T::class.java.classLoader!!)

        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }

inline fun <reified T : Enum<T>> Parcel.readEnum() =
    readString()?.let { enumValueOf<T>(it) }

inline fun <reified T : Enum<T>> Parcel.writeEnum(value: T?) =
    writeString(value?.name)

inline fun <reified T : Parcelable> Parcel.writeMap(map: Map<String, T>, flags: Int) {
    writeInt(map.size)
    map.forEach {
        writeString(it.key)
        writeParcelable(it.value, flags)
    }
}

inline fun <reified T : Parcelable> Parcel.writeMapOfList(map: Map<String, List<T>>) {
    writeInt(map.size)
    map.forEach {
        writeString(it.key)
        writeList(it.value)
    }
}

inline fun <reified T : Parcelable> Parcel.readMap(): Map<String, T> {
    val map = mutableMapOf<String, T>()
    val size = readInt()
    for (i in 0 until size) {
        val key = readString()
        val value = readParcelable<T>(T::class.java.classLoader)
        map[key!!] = value!!
    }
    return map
}

inline fun <reified T : Parcelable> Parcel.readMapOfList(): Map<String, List<T>> {
    val map = mutableMapOf<String, List<T>>()
    val size = readInt()
    for (i in 0 until size) {
        val key = readString()
        val value = mutableListOf<T>()
        readList(value as List<T>, T::class.java.classLoader)
        map[key!!] = value
    }
    return map
}
