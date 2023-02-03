package ai.ftech.fekyc.base.parcelable

import android.os.Parcelable

interface IParcelable: Parcelable, Cloneable {

    override fun describeContents(): Int {
        return 0
    }

    override fun clone(): Any {
        return super.clone()
    }
}
