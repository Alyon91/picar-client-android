package com.rpinferetti.picar

class Vehicle(var mSocket: MySocket) {

    fun moveForward(speed: Int) {
        mSocket.sendMessage(Command.MOVE_FORWARD + speed)
    }

    fun moveBackward(speed: Int) {
        mSocket.sendMessage(Command.MOVE_BACKWARD + speed)
    }

    fun moveStop() {
        mSocket.sendMessage(Command.STOP)
    }

    fun turnRight(speed: Int) {
        mSocket.sendMessage(Command.TURN_RIGHT + speed)
    }

    fun turnLeft(speed: Int) {
        mSocket.sendMessage(Command.TURN_LEFT + speed)
    }

    fun turnCenter() {
        mSocket.sendMessage(Command.TURN_CENTER)
    }

    fun rgbBlue() {
        mSocket.sendMessage(Command.RGB_BLUE)
    }

    fun rgbGreen() {
        mSocket.sendMessage(Command.RGB_GREEN)
    }

    fun rgbRed() {
        mSocket.sendMessage(Command.RGB_RED)
    }

    fun buzzer() {
        mSocket.sendMessage(Command.BUZZER_ALARM)
    }
}
