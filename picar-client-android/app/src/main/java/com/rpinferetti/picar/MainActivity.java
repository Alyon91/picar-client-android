package com.rpinferetti.picar;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class MainActivity extends AppCompatActivity implements ControlFragment.OnControlListener, ConnectFragment.OnConnectListener {

    private static final String TAG = "MainActivity";

    private static final int MOVE_SPEED = 100;
    private static final int TURN_SPEED = 40;

    private NavController mNavController;

    private UDPSocket mSocket;

    private Handler mHandler;
    private Runnable mGamepadRunnable;

    private Vehicle mVehicle;
    private Gamepad mGamepad;
    private GamepadMap mGamepadMap;
    private GamepadMap mPrevGamepadMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mSocket = UDPSocket.getInstance();
        mVehicle = new Vehicle();

        mGamepad = new Gamepad();
        mGamepad.setOnGamepadListener(new Gamepad.OnGamepadListener() {
            @Override
            public void onGamepadMapChanged(GamepadMap map) {
                if (map != null) {
                    mPrevGamepadMap = mGamepadMap;
                    mGamepadMap = map;
                }
            }
        });

        mHandler = new Handler();
        startGamepadPolling();
    }

    private void startGamepadPolling() {
        mGamepadRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mGamepadMap != null) {
                        if (mGamepadMap.getLeftStickX() == 0) {
                            mVehicle.turnCenter();
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.getLeftStickX() > 0) {
                            int speed = Math.round(mGamepadMap.getLeftStickX());
                            mVehicle.turnRight(speed);
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.getLeftStickX() < 0) {
                            int speed = Math.round(mGamepadMap.getLeftStickX());
                            mVehicle.turnLeft(Math.abs(speed));
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.getRightShoulderTrigger() == 0 &&
                                mGamepadMap.getLeftShoulderTrigger() == 0) {
                            mVehicle.moveStop();
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.getRightShoulderTrigger() > 0) {
                            int speed = Math.round(mGamepadMap.getRightShoulderTrigger());
                            mVehicle.moveForward(speed);
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.getLeftShoulderTrigger() > 0) {
                            int speed = Math.round(mGamepadMap.getLeftShoulderTrigger());
                            mVehicle.moveBackward(speed);
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.isButtonX()) {
                            mVehicle.rgbBlue();
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.isButtonA()) {
                            mVehicle.rgbGreen();
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.isButtonB()) {
                            mVehicle.rgbRed();
                            Thread.sleep(10);
                        }

                        if (mGamepadMap.isButtonY()) {
                            mVehicle.buzzer();
                            Thread.sleep(10);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mHandler.postDelayed(this, 100);
                }
            }
        };

        mGamepadRunnable.run();
    }

    private void stopGamepadPolling() {
        mHandler.removeCallbacks(mGamepadRunnable);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                mGamepad.handleButtonPressed(event);
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                mGamepad.processJoystickInput(event, i);
            }

            // Process the current movement sample in the batch (position -1)
            mGamepad.processJoystickInput(event, -1);
            return true;
        }

        return super.dispatchGenericMotionEvent(event);
    }


    @Override
    public void onControlButtonPressed(int button) {
        switch (button) {
            case STOP:
                mVehicle.moveStop();
                break;

            case FORWARD:
                mVehicle.moveForward(MOVE_SPEED);
                break;

            case BACKWARD:
                mVehicle.moveBackward(MOVE_SPEED);
                break;

            case LEFT:
                mVehicle.turnLeft(TURN_SPEED);
                break;

            case RIGHT:
                mVehicle.turnRight(TURN_SPEED);
                break;

            case CENTER:
                mVehicle.turnCenter();
                break;

            case RGB_BLUE:
                mVehicle.rgbBlue();
                break;

            case RGB_GREEN:
                mVehicle.rgbGreen();
                break;

            case RGB_RED:
                mVehicle.rgbRed();
                break;

            case BUZZER:
                mVehicle.buzzer();
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
                mNavController.navigate(R.id.action_connectFragment_to_controlFragment);
            }

            @Override
            public void onConnectFailure() {
                showToast("Impossibile connettersi");
            }
        });
        mSocket.connect(address, port);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGamepadPolling();
    }
}