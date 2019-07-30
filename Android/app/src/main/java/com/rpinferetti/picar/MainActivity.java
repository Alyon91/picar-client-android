package com.rpinferetti.picar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements ControlFragment.OnControlListener, ConnectFragment.OnConnectListener {

    private static final String TAG = "MainActivity";

    private static final int MOVE_SPEED = 100;
    private static final int TURN_SPEED = 40;

    private static Socket mSocket;
    private static Writer mBufferOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void moveForward() {
        int i = 3;
        while (i > 0) {
            sendMessage(Command.MOVE_FORWARD + MOVE_SPEED / i);
            i--;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveBackward() {
        int i = 3;
        while (i > 0) {
            sendMessage(Command.MOVE_BACKWARD + MOVE_SPEED / i);
            i--;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveStop() {
        sendMessage(Command.STOP);
    }

    private void sendMessage(final String msg) {
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

    public void getListOfConnectedDevice() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                BufferedReader br = null;
                boolean isFirstLine = true;

                try {
                    br = new BufferedReader(new FileReader("/proc/net/arp"));
                    String line;

                    while ((line = br.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue;
                        }

                        String[] splitted = line.split(" +");

                        if (splitted != null && splitted.length >= 4) {

                            String ipAddress = splitted[0];
                            String macAddress = splitted[3];

                            boolean isReachable = InetAddress.getByName(
                                    splitted[0]).isReachable(500);  // this is network call so we cant do that on UI thread, so i take background thread.
                            if (isReachable) {
                                Log.d("Device Information", ipAddress + " : "
                                        + macAddress);
                            }

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void onControlButtonPressed(int button) {
        switch (button) {
            case STOP:
                moveStop();
                break;

            case FORWARD:
                moveForward();
                break;

            case BACKWARD:
                moveBackward();
                break;

            case LEFT:
                sendMessage(Command.TURN_LEFT + TURN_SPEED);
                break;

            case RIGHT:
                sendMessage(Command.TURN_RIGHT + TURN_SPEED);
                break;

            case CENTER:
                sendMessage(Command.TURN_CENTER);
                break;

            case RGB_BLUE:
                sendMessage(Command.RGB_BLUE);
                break;

            case RGB_GREEN:
                sendMessage(Command.RGB_GREEN);
                break;

            case RGB_RED:
                sendMessage(Command.RGB_RED);
                break;

            case BUZZER:
                sendMessage(Command.BUZZER_ALARM);
                break;

            default:
                Toast.makeText(this, "Invalid command", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnect(String address, int port) {
        ConnectTask task = new ConnectTask(address, port);
        task.setOnConnectTaskListener(new ConnectTask.OnConnectTaskListener() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MainActivity.this, "Connesso", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.action_connectFragment_to_controlFragment);
            }

            @Override
            public void OnFailure() {
                Toast.makeText(MainActivity.this, "Impossibile connettersi", Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
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
                mListener.OnSuccess();
            } else {
                mListener.OnFailure();
            }
        }

        void setOnConnectTaskListener(OnConnectTaskListener listener) {
            mListener = listener;
        }

        public interface OnConnectTaskListener {
            void OnSuccess();

            void OnFailure();
        }
    }
}