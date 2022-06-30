package ai.ftech.ekyc.domain.event

import ai.ftech.base.parcelable.parcelableCreator
import android.os.Parcel
import android.os.Parcelable

class EkycEvent() : IFbaseEvent, Parcelable {
    var code: Int = 0
    var message: String = ""

    constructor(parcel: Parcel) : this() {
        this.code = parcel.readInt()
        this.message = parcel.readString() ?: ""
    }

    override fun writeToParcel(dest: Parcel, p1: Int) {
        dest.writeInt(code)
        dest.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::EkycEvent)
    }
}
