package com.rpinferetti.picar

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import kotlin.math.abs

class Gamepad {
    private val mGamepadMap: GamepadMap = GamepadMap()
    private var mListener: OnGamepadListener? = null

    fun handleButtonPressed(event: KeyEvent) {
        val action = event.action

        when (event.keyCode) {
            KeyEvent.KEYCODE_BUTTON_X -> mGamepadMap.isButtonX = action == KeyEvent.ACTION_DOWN

            KeyEvent.KEYCODE_BUTTON_A -> mGamepadMap.isButtonA = action == KeyEvent.ACTION_DOWN

            KeyEvent.KEYCODE_BUTTON_Y -> mGamepadMap.isButtonY = action == KeyEvent.ACTION_DOWN

            KeyEvent.KEYCODE_BUTTON_B -> mGamepadMap.isButtonB = action == KeyEvent.ACTION_DOWN

            KeyEvent.KEYCODE_BUTTON_R1 -> mGamepadMap.isButtonR1 = action == KeyEvent.ACTION_DOWN

            KeyEvent.KEYCODE_BUTTON_L1 -> mGamepadMap.isButtonL1 = action == KeyEvent.ACTION_DOWN
        }

        mListener?.onGamepadMapChanged(mGamepadMap)
    }

    private fun getCenteredAxis(
        event: MotionEvent, device: InputDevice,
        axis: Int, historyPos: Int
    ): Float {
        val range = device.getMotionRange(axis, event.source)

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            val flat = range.flat
            val value = if (historyPos < 0)
                event.getAxisValue(axis)
            else
                event.getHistoricalAxisValue(axis, historyPos)

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (abs(value) > flat) {
                return value
            }
        }
        return 0f
    }

    fun processJoystickInput(event: MotionEvent, historyPos: Int) {
        val inputDevice = event.device

        // Left control stick and hat
        // Horizontal distance
        var lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos)
        if (lx == 0f) {
            lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos)
        }
        // Vertical distance
        var ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos)
        if (ly == 0f) {
            ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_Y, historyPos)
        }

        // Right control stick
        // Horizontal distance
        val rx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos)
        // Vertical distance
        val ry = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos)

        // Shoulder triggers
        // Left
        val lt = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_BRAKE)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_BRAKE, historyPos)
        // Right
        val rt = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_GAS)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_GAS, historyPos)

        mGamepadMap.leftStickX = lx * 100
        mGamepadMap.leftStickY = ly * 100
        mGamepadMap.rightStickX = rx * 100
        mGamepadMap.rightStickY = ry * 100
        mGamepadMap.leftShoulderTrigger = lt * 100
        mGamepadMap.rightShoulderTrigger = rt * 100

        mListener?.onGamepadMapChanged(mGamepadMap)
    }

    fun setOnGamepadListener(listener: OnGamepadListener) {
        mListener = listener
    }

    interface OnGamepadListener {
        fun onGamepadMapChanged(map: GamepadMap)
    }
}
