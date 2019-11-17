package com.rpinferetti.picar

import android.os.AsyncTask
import android.util.Log

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPSocket(address: String?, port: Int) : MySocket(address, port) {

    private var mSocket: DatagramSocket? = null
    private var mAddress: InetAddress = InetAddress.getByName(address)

    override fun connect() {
        if (mSocket != null)
            disconnect()

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
        mSocket?.close()
    }

    override fun sendMessage(msg: String) {
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

    companion object {
        private const val TAG = "UDPSocket"
    }
}
