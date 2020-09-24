package com.rpinferetti.picar

import com.rpinferetti.gamepad.Gamepad
import com.rpinferetti.gamepad.GamepadButton
import kotlin.math.abs
import kotlin.math.roundToInt

class GamepadControl(private var vehicle: Vehicle) {
    private var isButtonYPressed = false
    private var isStopped = true
    private var isCentered = true

    fun handleInput(gamepad: Gamepad) {
        val map = gamepad.getGamepadMap()
        vehicle.apply {
            if (map[GamepadButton.STICK_LEFT_X] == 0f && !isCentered) {
                isCentered = true
                turnCenter()
            }

            if (map[GamepadButton.STICK_LEFT_X]!! > 0) {
                isCentered = false
                val speed = map[GamepadButton.STICK_LEFT_X]!!.roundToInt()
                turnRight(speed)
            }

            if (map[GamepadButton.STICK_LEFT_X]!! < 0) {
                isCentered = false
                val speed = map[GamepadButton.STICK_LEFT_X]!!.roundToInt()
                turnLeft(abs(speed))
            }

            if (map[GamepadButton.TRIGGER_LEFT] == 0f &&
                    map[GamepadButton.TRIGGER_RIGHT] == 0f && !isStopped) {
                isStopped = true
                moveStop()
            }

            if (map[GamepadButton.TRIGGER_LEFT]!! > 0f) {
                isStopped = false
                val speed = map[GamepadButton.TRIGGER_LEFT]!!.roundToInt()
                moveBackward(speed)
            }

            if (map[GamepadButton.TRIGGER_RIGHT]!! > 0f) {
                isStopped = false
                val speed = map[GamepadButton.TRIGGER_RIGHT]!!.roundToInt()
                moveForward(speed)
            }

            if (map[GamepadButton.BUTTON_X] == 1f) {
                rgbBlue()
            }

            if (map[GamepadButton.BUTTON_A] == 1f) {
                rgbGreen()
            }

            if (map[GamepadButton.BUTTON_B] == 1f) {
                rgbRed()
            }

            if (map[GamepadButton.BUTTON_Y] == 1f && !isButtonYPressed) {
                isButtonYPressed = true
                buzzer()
            }

            if (map[GamepadButton.BUTTON_Y] == 0f && isButtonYPressed) {
                isButtonYPressed = false
                buzzer()
            }
        }
    }
}