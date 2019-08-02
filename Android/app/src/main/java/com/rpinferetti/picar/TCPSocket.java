package com.rpinferetti.picar;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPSocket {
    private static final String TAG = "TCPSocket";

    private OnConnectSocketListener mListener;

    private static Socket mSocket;
    private static Writer mBufferOut;


    void connect(String address, int port) {
        ConnectTask task = new ConnectTask(address, port);
        task.setOnConnectTaskListener(new ConnectTask.OnConnectTaskListener() {
            @Override
            public void onSuccess() {
                mListener.onSuccess();
            }

            @Override
            public void onFailure() {
                mListener.onFailure();
            }
        });
        task.execute();
    }

    public void disconnect() {
        try {
            mBufferOut.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(final String msg) {
        if (mSocket != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mBufferOut != null) {
                        try {
                            Log.d(TAG, msg);
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

    void setOnConnectSocketListener(OnConnectSocketListener listener) {
        mListener = listener;
    }

    public interface OnConnectSocketListener {
        void onSuccess();

        void onFailure();
    }

    public static class ConnectTask extends AsyncTask<Void, Void, Void> {
        private String address;
        private int port;
        private OnConnectTaskListener mListener;

        ConnectTask(String address, int port) {
            this.address = address;
            this.port = port;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InetAddress serverAddr = InetAddress.getByName(address);
                mSocket = new Socket(serverAddr, port);
                mBufferOut = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mSocket != null) {
                mListener.onSuccess();
            } else {
                mListener.onFailure();
            }
        }

        void setOnConnectTaskListener(OnConnectTaskListener listener) {
            mListener = listener;
        }

        public interface OnConnectTaskListener {
            void onSuccess();

            void onFailure();
        }
    }
}
