package com.rpinferetti.picar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;


public class MainActivity extends AppCompatActivity implements ControlFragment.OnControlListener, ConnectFragment.OnConnectListener {

    private static final String TAG = "MainActivity";

    private static final int MOVE_SPEED = 100;
    private static final int TURN_SPEED = 40;

    private static UDPSocket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket = new UDPSocket();
    }

    private void moveForward() {
        int i = 3;
        while (i > 0) {
            mSocket.sendMessage(Command.MOVE_FORWARD + MOVE_SPEED / i);
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
            mSocket.sendMessage(Command.MOVE_BACKWARD + MOVE_SPEED / i);
            i--;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveStop() {
        mSocket.sendMessage(Command.STOP);
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
                mSocket.sendMessage(Command.TURN_LEFT + TURN_SPEED);
                break;

            case RIGHT:
                mSocket.sendMessage(Command.TURN_RIGHT + TURN_SPEED);
                break;

            case CENTER:
                mSocket.sendMessage(Command.TURN_CENTER);
                break;

            case RGB_BLUE:
                mSocket.sendMessage(Command.RGB_BLUE);
                break;

            case RGB_GREEN:
                mSocket.sendMessage(Command.RGB_GREEN);
                break;

            case RGB_RED:
                mSocket.sendMessage(Command.RGB_RED);
                break;

            case BUZZER:
                mSocket.sendMessage(Command.BUZZER_ALARM);
                break;

            default:
                showToast("Invalid command");
        }
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnect(String address, int port) {
        mSocket.setOnUDPSocketListener(new UDPSocket.OnUDPSocketListener() {
            @Override
            public void onConnectSuccess() {
                showToast("Connesso");
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.action_connectFragment_to_controlFragment);
            }

            @Override
            public void onConnectFailure() {
                showToast("Impossibile connettersi");
            }
        });
        mSocket.connect(address, port);
    }

}