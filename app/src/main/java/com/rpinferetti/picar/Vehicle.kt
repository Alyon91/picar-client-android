package com.rpinferetti.picar

class Vehicle(var mSocket: MySocket) {

    fun moveForward(speed: Int) {
        mSocket.sendMessage(MOVE_FORWARD + speed)
    }

    fun moveBackward(speed: Int) {
        mSocket.sendMessage(MOVE_BACKWARD + speed)
    }

    fun moveStop() {
        mSocket.sendMessage(STOP)
    }

    fun turnRight(speed: Int) {
        mSocket.sendMessage(TURN_RIGHT + speed)
    }

    fun turnLeft(speed: Int) {
        mSocket.sendMessage(TURN_LEFT + speed)
    }

    fun turnCenter() {
        mSocket.sendMessage(TURN_CENTER)
    }

    fun rgbBlue() {
        mSocket.sendMessage(RGB_BLUE)
    }

    fun rgbGreen() {
        mSocket.sendMessage(RGB_GREEN)
    }

    fun rgbRed() {
        mSocket.sendMessage(RGB_RED)
    }

    fun buzzer() {
        mSocket.sendMessage(BUZZER_ALARM)
    }

    companion object {
        const val MOVE_FORWARD = ">Move Forward"
        const val MOVE_BACKWARD = ">Move Backward"
        const val TURN_LEFT = ">Turn Left"
        const val TURN_RIGHT = ">Turn Right"
        const val STOP = ">Move Stop"
        const val TURN_CENTER = ">Turn Center90"
        const val CAMERA_UP = ">Camera Up"
        const val CAMERA_DOWN = ">Camera Down"
        const val CAMERA_LEFT = ">Camera Left"
        const val CAMERA_RIGHT = ">Camera Right"
        const val CAMERA_STOP = ">Camera Stop"
        const val CAMERA_CENTER = ">Camera Center"

        const val SPEED_SLIDER = ">Speed Slider"
        const val DIR_SLIDER = ">Direction Slider"
        const val CAMERA_SLIDER = ">Camera Slider"

        const val RGB_RED = ">RGB Red"
        const val RGB_GREEN = ">RGB Green"
        const val RGB_BLUE = ">RGB Blue"

        const val BUZZER_ALARM = ">Buzzer Alarm"
        const val ULTRASONIC = ">Ultrasonic"
        const val SONIC_LEFT = CAMERA_LEFT
        const val SONIC_RIGHT = CAMERA_RIGHT
    }
}
