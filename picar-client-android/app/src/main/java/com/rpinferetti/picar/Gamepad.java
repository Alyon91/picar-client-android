package com.rpinferetti.picar;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Gamepad {

    private GamepadMap mGamepadMap;
    private OnGamepadListener mListener;

    public Gamepad() {
        mGamepadMap = new GamepadMap();
    }

    public void handleButtonPressed(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_BUTTON_X:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonX(true);
                else
                    mGamepadMap.setButtonX(false);
                break;

            case KeyEvent.KEYCODE_BUTTON_A:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonA(true);
                else
                    mGamepadMap.setButtonA(false);
                break;

            case KeyEvent.KEYCODE_BUTTON_Y:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonY(true);
                else
                    mGamepadMap.setButtonY(false);
                break;

            case KeyEvent.KEYCODE_BUTTON_B:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonB(true);
                else
                    mGamepadMap.setButtonB(false);
                break;

            case KeyEvent.KEYCODE_BUTTON_R1:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonR1(true);
                else
                    mGamepadMap.setButtonR1(false);
                break;

            case KeyEvent.KEYCODE_BUTTON_L1:
                if (action == KeyEvent.ACTION_DOWN)
                    mGamepadMap.setButtonL1(true);
                else
                    mGamepadMap.setButtonL1(false);
                break;
        }

        mListener.onGamepadMapChanged(mGamepadMap);
    }

    private float getCenteredAxis(MotionEvent event, InputDevice device,
                                  int axis, int historyPos) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value = historyPos < 0 ? event.getAxisValue(axis) :
                    event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    public void processJoystickInput(MotionEvent event, int historyPos) {
        InputDevice inputDevice = event.getDevice();

        // Left control stick and hat
        // Horizontal distance
        float lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);
        if (lx == 0) {
            lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos);
        }
        // Vertical distance
        float ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);
        if (ly == 0) {
            ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_Y, historyPos);
        }

        // Right control stick
        // Horizontal distance
        float rx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);
        // Vertical distance
        float ry = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);

        // Shoulder triggers
        // Left
        float lt = historyPos < 0 ? event.getAxisValue(MotionEvent.AXIS_BRAKE) :
                event.getHistoricalAxisValue(MotionEvent.AXIS_BRAKE, historyPos);
        // Right
        float rt = historyPos < 0 ? event.getAxisValue(MotionEvent.AXIS_GAS) :
                event.getHistoricalAxisValue(MotionEvent.AXIS_GAS, historyPos);

        mGamepadMap.setLeftStickX(lx * 100);
        mGamepadMap.setLeftStickY(ly * 100);
        mGamepadMap.setRightStickX(rx * 100);
        mGamepadMap.setRightStickY(ry * 100);
        mGamepadMap.setLeftShoulderTrigger(lt * 100);
        mGamepadMap.setRightShoulderTrigger(rt * 100);

        mListener.onGamepadMapChanged(mGamepadMap);
    }

    public void setOnGamepadListener(OnGamepadListener listener) {
        mListener = listener;
    }

    public interface OnGamepadListener {
        void onGamepadMapChanged(GamepadMap map);
    }
}
