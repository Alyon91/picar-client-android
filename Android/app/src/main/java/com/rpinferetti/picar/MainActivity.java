package com.rpinferetti.picar;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SERVER_IP = "192.168.1.180";
    private static final int SERVER_PORT = 12345;

    private static final int MOVE_SPEED = 100;
    private static final int TURN_SPEED = 40;

    private Socket mSocket;
    private Writer mBufferOut;
    //private BufferedReader mBufferIn;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initButtons() {
        ImageButton forwardButton = findViewById(R.id.btn_forward);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "mForwardButton pressed");
                    moveForward();
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "mForwardButton released");
                    moveStop();
                    return true;
                }
                return false;
            }
        });

        ImageButton backwardButton = findViewById(R.id.btn_backward);
        backwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "mBackwardButton pressed");
                    moveBackward();
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "mBackwardButton released");
                    moveStop();
                    return true;
                }
                return false;
            }
        });

        ImageButton turnLeftButton = findViewById(R.id.btn_turn_left);
        turnLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage(Command.TURN_LEFT + TURN_SPEED);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage(Command.TURN_CENTER);
                    return true;
                }
                return false;
            }
        });

        ImageButton turnRightButton = findViewById(R.id.btn_turn_right);
        turnRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage(Command.TURN_RIGHT + TURN_SPEED);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage(Command.TURN_CENTER);
                    return true;
                }
                return false;
            }
        });

        ImageButton buzzerButton = findViewById(R.id.btn_buzzer);
        buzzerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sendMessage(Command.BUZZER_ALARM);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sendMessage(Command.BUZZER_ALARM);
                    return true;
                }
                return false;
            }
        });
    }

    private void connect() {
        new ConnectTask().execute();
    }

    private void disconnect() {
        try {
            mBufferOut.close();
            mSocket.close();
            Toast.makeText(MainActivity.this, "Disconnesso", Toast.LENGTH_SHORT).show();
            isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onRedButtonClicked(View view) {
        sendMessage(Command.RGB_RED);
    }

    public void onGreenButtonClicked(View view) {
        sendMessage(Command.RGB_GREEN);
    }

    public void onBlueButtonClicked(View view) {
        sendMessage(Command.RGB_BLUE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_connect:
                if (!isConnected) {
                    connect();
                } else {
                    disconnect();
                }
                break;
                /*
            case R.id.item_camera:
                startCameraActivity();
                break;
                */
        }
        return super.onOptionsItemSelected(item);
    }

    public class ConnectTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                mSocket = new Socket(serverAddr, SERVER_PORT);
                mBufferOut = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), StandardCharsets.UTF_8));
                //mBufferIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mSocket != null) {
                isConnected = true;
                Toast.makeText(MainActivity.this, "Connesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Impossibile connettersi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}