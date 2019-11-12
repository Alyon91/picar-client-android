package com.rpinferetti.picar;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class TCPSocket {
    private static final String TAG = "TCPSocket";

    private static TCPSocket mInstance;

    private Socket mSocket;
    private Writer mBufferOut;
    private InetAddress mServerAddr;
    private int mPort;

    private OnTCPSocketListener mListener;

    public TCPSocket() {
    }

    public static TCPSocket getInstance() {
        if (mInstance == null)
            synchronized (TCPSocket.class) {
                if (mInstance == null)
                    mInstance = new TCPSocket();
            }
        return mInstance;
    }

    public void connect(String address, int port) {
        if (mSocket != null)
            disconnect();

        try {
            mServerAddr = InetAddress.getByName(address);
            mPort = port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ConnectTask task = new ConnectTask(mServerAddr, mPort, new ConnectTask.OnConnectTaskListener() {
            @Override
            public void onSuccess(Socket socket, Writer bufferOut) {
                mSocket = socket;
                mBufferOut = bufferOut;
                mListener.onConnectSuccess();
            }

            @Override
            public void onFailure() {
                mListener.onConnectFailure();
            }
        });

        task.execute();
    }

    public void sendMessage(final String msg) {
        Log.d(TAG, msg);
        if (mSocket != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mBufferOut != null) {
                        try {
                            mBufferOut.write(msg);
                            mBufferOut.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    public void disconnect() {
        try {
            mBufferOut.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnTCPSocketListener(OnTCPSocketListener listener) {
        mListener = listener;
    }

    public interface OnTCPSocketListener {
        void onConnectSuccess();

        void onConnectFailure();
    }

    public static class ConnectTask extends AsyncTask<Void, Void, Void> {
        private Socket socket;
        private Writer bufferOut;
        private InetAddress address;
        private int port;
        private OnConnectTaskListener listener;

        ConnectTask(InetAddress address, int port, OnConnectTaskListener listener) {
            this.address = address;
            this.port = port;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(address, port);
                bufferOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (socket != null)
                listener.onSuccess(socket, bufferOut);
            else
                listener.onFailure();
        }

        public interface OnConnectTaskListener {
            void onSuccess(Socket socket, Writer bufferOut);

            void onFailure();
        }
    }
}
