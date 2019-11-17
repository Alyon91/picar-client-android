package com.rpinferetti.picar

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

open class MySocket(var address: String?, var port: Int) : Parcelable {

    var onMySocketListener: OnMySocketListener? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    open fun connect() {
        Log.i(TAG, "connect() is called")
    }

    open fun disconnect() {
        Log.i(TAG, "disconnect() is called")
    }

    open fun sendMessage(msg: String) {
        Log.i(TAG, msg)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeInt(port)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MySocket> {
        private const val TAG = "MySocket"

        override fun createFromParcel(parcel: Parcel): MySocket {
            return MySocket(parcel)
        }

        override fun newArray(size: Int): Array<MySocket?> {
            return arrayOfNulls(size)
        }
    }


    interface OnMySocketListener {
        fun onConnectSuccess()

        fun onConnectFailure()
    }
}
