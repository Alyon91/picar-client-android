package com.rpinferetti.picar;

import android.util.Log;

public class Vehicle {
    private static final String TAG = "Vehicle";

    private UDPSocket mSocket;

    public Vehicle() {
        mSocket = UDPSocket.getInstance();
    }

    public void moveForward(int speed) {
        mSocket.sendMessage(Command.MOVE_FORWARD + speed);
    }

    public void moveBackward(int speed) {
        mSocket.sendMessage(Command.MOVE_BACKWARD + speed);
    }

    public void moveStop() {
        mSocket.sendMessage(Command.STOP);
    }

    public void turnRight(int speed) {
        mSocket.sendMessage(Command.TURN_RIGHT + speed);
    }

    public void turnLeft(int speed) {
        mSocket.sendMessage(Command.TURN_LEFT + speed);
    }

    public void turnCenter() {
        mSocket.sendMessage(Command.TURN_CENTER);
    }

    public void rgbBlue() {
        mSocket.sendMessage(Command.RGB_BLUE);
    }

    public void rgbGreen() {
        mSocket.sendMessage(Command.RGB_GREEN);
    }

    public void rgbRed() {
        mSocket.sendMessage(Command.RGB_RED);
    }

    public void buzzer() {
        mSocket.sendMessage(Command.BUZZER_ALARM);
    }
}
