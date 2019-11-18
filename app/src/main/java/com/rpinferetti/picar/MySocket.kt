package com.rpinferetti.picar

import android.os.Parcel
import android.os.Parcelable

abstract class MySocket(var address: String?, var port: Int) : Parcelable {

    var onMySocketListener: OnMySocketListener? = null

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readInt())

    abstract fun connect()

    abstract fun disconnect()

    abstract fun sendMessage(msg: String)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(address)
        dest?.writeInt(port)
    }

    interface OnMySocketListener {
        fun onConnectSuccess()

        fun onConnectFailure()
    }
}
