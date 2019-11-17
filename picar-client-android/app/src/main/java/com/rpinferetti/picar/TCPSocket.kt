package com.rpinferetti.picar

import android.os.AsyncTask
import android.util.Log

import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.StandardCharsets

class TCPSocket(address: String?, port: Int) : MySocket(address, port) {

    private var mAddress = InetAddress.getByName(address)
    private var mSocket: Socket? = null
    private var mBufferOut: Writer? = null

    override fun connect() {
        if (mSocket != null)
            disconnect()

        val task = ConnectTask(mAddress, port, object : ConnectTask.OnConnectTaskListener {
            override fun onSuccess(socket: Socket, bufferOut: Writer?) {
                mSocket = socket
                mBufferOut = bufferOut
                onMySocketListener?.onConnectSuccess()
            }

            override fun onFailure() {
                onMySocketListener?.onConnectFailure()
            }
        })

        task.execute()
    }

    override fun sendMessage(msg: String) {
        if (mSocket != null) {
            Thread(Runnable {
                mBufferOut?.let {
                    try {
                        it.write(msg)
                        it.flush()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }).start()
        }
    }

    override fun disconnect() {
        try {
            mBufferOut?.close()
            mSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    class ConnectTask internal constructor(
        private val address: InetAddress,
        private val port: Int,
        private val listener: OnConnectTaskListener
    ) : AsyncTask<Unit, Unit, Unit>() {
        private var socket: Socket? = null
        private var bufferOut: Writer? = null

        override fun doInBackground(vararg units: Unit): Unit? {
            try {
                socket = Socket(address, port)
                bufferOut = BufferedWriter(
                    OutputStreamWriter(
                        socket!!.getOutputStream(),
                        StandardCharsets.UTF_8
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return Unit
        }

        override fun onPostExecute(aUnit: Unit) {
            super.onPostExecute(aUnit)
            if (socket != null)
                listener.onSuccess(socket!!, bufferOut)
            else
                listener.onFailure()
        }

        interface OnConnectTaskListener {
            fun onSuccess(socket: Socket, bufferOut: Writer?)

            fun onFailure()
        }
    }

    companion object {
        private const val TAG = "TCPSocket"
    }
}
