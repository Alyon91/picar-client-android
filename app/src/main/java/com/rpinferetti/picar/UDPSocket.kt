package com.rpinferetti.picar

import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.util.Log

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPSocket : MySocket, Parcelable {

    private var mSocket: DatagramSocket? = null
    private var mAddress: InetAddress = InetAddress.getByName(address)

    constructor(address: String, port: Int) : super(address, port)

    override fun connect() {
        val task = ConnectTask(mAddress, port, object : ConnectTask.OnConnectTaskListener {
            override fun onSuccess(socket: DatagramSocket) {
                mSocket = socket
                onMySocketListener?.onConnectSuccess()
            }

            override fun onFailure() {
                onMySocketListener?.onConnectFailure()
            }
        })

        task.execute()
    }

    override fun disconnect() {
        if (mSocket != null) {
            Thread(Runnable {
                try {
                    mSocket?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }

    override fun sendMessage(msg: String) {
        Log.d("UDPSocket", msg)
        if (mSocket != null) {
            Thread(Runnable {
                val buf = msg.toByteArray()
                val packet = DatagramPacket(buf, buf.size, mAddress, port)
                try {
                    mSocket?.send(packet)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }

    class ConnectTask internal constructor(
        private val address: InetAddress,
        private val port: Int,
        private val listener: OnConnectTaskListener
    ) : AsyncTask<Unit, Unit, Unit>() {
        private var socket: DatagramSocket? = null

        override fun doInBackground(vararg units: Unit): Unit? {
            try {
                socket = DatagramSocket(port)
                socket?.connect(address, port)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return Unit
        }

        override fun onPostExecute(aUnit: Unit) {
            super.onPostExecute(aUnit)
            if (socket != null)
                listener.onSuccess(socket!!)
            else
                listener.onFailure()
        }

        interface OnConnectTaskListener {
            fun onSuccess(socket: DatagramSocket)

            fun onFailure()
        }
    }

    // PARCELABLE IMPLEMENTATION

    constructor(parcel: Parcel) : super(parcel)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<UDPSocket> {
        override fun createFromParcel(parcel: Parcel): UDPSocket {
            return UDPSocket(parcel)
        }

        override fun newArray(size: Int): Array<UDPSocket?> {
            return arrayOfNulls(size)
        }
    }
}
