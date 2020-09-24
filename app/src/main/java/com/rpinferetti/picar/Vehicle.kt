package com.rpinferetti.picar

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Vehicle() : Parcelable {
    private var socket: MySocket? = null

    constructor(parcel: Parcel) : this()

    fun setSocket(socket: MySocket) {
        this.socket = socket
    }

    fun moveForward(speed: Int) {
        Log.i(TAG, "moveForward - $speed")
        socket?.sendMessage(CREATOR.MOVE_FORWARD + speed)
    }

    fun moveBackward(speed: Int) {
        Log.i(TAG, "moveBackward - $speed")
        socket?.sendMessage(MOVE_BACKWARD + speed)
    }

    fun moveStop() {
        Log.i(TAG, "moveStop")
        socket?.sendMessage(STOP)
    }

    fun turnRight(speed: Int) {
        Log.i(TAG, "turnRight - $speed")
        socket?.sendMessage(TURN_RIGHT + speed)
    }

    fun turnLeft(speed: Int) {
        Log.i(TAG, "turnLeft - $speed")
        socket?.sendMessage(TURN_LEFT + speed)
    }

    fun turnCenter() {
        Log.i(TAG, "turnCenter")
        socket?.sendMessage(TURN_CENTER)
    }

    fun rgbBlue() {
        Log.i(TAG, "rgbBlue")
        socket?.sendMessage(RGB_BLUE)
    }

    fun rgbGreen() {
        Log.i(TAG, "rgbGreen")
        socket?.sendMessage(RGB_GREEN)
    }

    fun rgbRed() {
        Log.i(TAG, "rgbRed")
        socket?.sendMessage(RGB_RED)
    }

    fun buzzer() {
        Log.i(TAG, "buzzer")
        socket?.sendMessage(BUZZER_ALARM)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(socket, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Vehicle> {
        override fun createFromParcel(parcel: Parcel): Vehicle {
            return Vehicle(parcel)
        }

        override fun newArray(size: Int): Array<Vehicle?> {
            return arrayOfNulls(size)
        }

        const val TAG = "Vehicle"

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
