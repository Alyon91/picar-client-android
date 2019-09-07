package com.rpinferetti.picar;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPSocket {
    private static final String TAG = "UDPSocket";

    private static DatagramSocket mSocket;
    private static InetAddress mServerAddr;
    private static int mPort;

    private OnUDPSocketListener mListener;

    public void connect(String address, int port) {
        try {
            mServerAddr = InetAddress.getByName(address);
            mPort = port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ConnectTask task = new ConnectTask(address, port, new ConnectTask.OnConnectTaskListener() {
            @Override
            public void onSuccess() {
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
        private String address;
        private int port;
        private OnConnectTaskListener listener;

        ConnectTask(String address, int port, OnConnectTaskListener listener) {
            this.address = address;
            this.port = port;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mSocket = new DatagramSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mSocket != null)
                listener.onSuccess();
            else
                listener.onFailure();
        }

        public interface OnConnectTaskListener {
            void onSuccess();

            void onFailure();
        }
    }
}
