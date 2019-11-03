package com.rpinferetti.picar;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPSocket {
    private static final String TAG = "UDPSocket";

    private static UDPSocket mInstance;

    private DatagramSocket mSocket;
    private InetAddress mServerAddr;
    private int mPort;

    private OnUDPSocketListener mListener;

    public UDPSocket() {
    }

    public static UDPSocket getInstance() {
        if (mInstance == null)
            synchronized (UDPSocket.class) {
                if (mInstance == null)
                    mInstance = new UDPSocket();
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
            public void onSuccess(DatagramSocket socket) {
                mSocket = socket;
                mListener.onConnectSuccess();
            }

            @Override
            public void onFailure() {
                mListener.onConnectFailure();
            }
        });

        task.execute();
    }

    public void disconnect() {
        mSocket.close();
    }

    public void sendMessage(final String msg) {
        Log.d(TAG, msg);
        if (mSocket != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buf = (msg).getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, mServerAddr, mPort);
                    try {
                        mSocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void setOnUDPSocketListener(OnUDPSocketListener listener) {
        mListener = listener;
    }

    public interface OnUDPSocketListener {
        void onConnectSuccess();

        void onConnectFailure();
    }

    public static class ConnectTask extends AsyncTask<Void, Void, Void> {
        private DatagramSocket socket;
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
                socket = new DatagramSocket(port);
                socket.connect(address, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (socket != null)
                listener.onSuccess(socket);
            else
                listener.onFailure();
        }

        public interface OnConnectTaskListener {
            void onSuccess(DatagramSocket socket);

            void onFailure();
        }
    }
}
