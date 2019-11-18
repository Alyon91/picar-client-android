package com.rpinferetti.picar

import android.os.AsyncTask
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.*

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets

class TCPSocket : MySocket, Parcelable {

    private var mSocket: Socket? = null
    private var mBufferOut: PrintWriter? = null


    constructor(address: String, port: Int) : super(address, port)

    override fun connect() {
        val addr = InetAddress.getByName(address)
        val task = ConnectTask(addr, port, object : ConnectTask.OnConnectTaskListener {
            override fun onSuccess(socket: Socket) {
                mSocket = socket
                mBufferOut = PrintWriter(socket.getOutputStream(), true)
                onMySocketListener?.onConnectSuccess()
            }

            override fun onFailure() {
                onMySocketListener?.onConnectFailure()
            }
        })

        task.execute()
    }

    override fun sendMessage(msg: String) {
        Log.d("TCPSocket", msg)
        if (mSocket != null) {
            Thread(Runnable {
                mBufferOut?.let {
                    try {
                        it.println(msg)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }).start()
        }
    }

    override fun disconnect() {
        if (mSocket != null) {
            Thread(Runnable {
                try {
                    mBufferOut?.close()
                    mSocket?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }


    // CONNECT ASYNCTASK

    class ConnectTask internal constructor(
        private val address: InetAddress,
        private val port: Int,
        private val listener: OnConnectTaskListener
    ) : AsyncTask<Unit, Unit, Unit>() {
        private var socket: Socket? = null

        override fun doInBackground(vararg units: Unit): Unit? {
            try {
                socket = Socket()
                socket?.connect(InetSocketAddress(address, port), 3 * 1000)
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
            fun onSuccess(socket: Socket)

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


    companion object CREATOR : Parcelable.Creator<TCPSocket> {
        override fun createFromParcel(parcel: Parcel): TCPSocket {
            return TCPSocket(parcel)
        }

        override fun newArray(size: Int): Array<TCPSocket?> {
            return arrayOfNulls(size)
        }
    }
}
