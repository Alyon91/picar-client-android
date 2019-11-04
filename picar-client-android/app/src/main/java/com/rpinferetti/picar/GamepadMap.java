package com.rpinferetti.picar;

public class GamepadMap {
    private float leftStickX;
    private float leftStickY;

    private float rightStickX;
    private float rightStickY;

    private float leftShoulderTrigger;
    private float rightShoulderTrigger;

    public float getLeftStickX() {
        return leftStickX;
    }

    public void setLeftStickX(float leftStickX) {
        this.leftStickX = leftStickX;
    }

    public float getLeftStickY() {
        return leftStickY;
    }

    public void setLeftStickY(float leftStickY) {
        this.leftStickY = leftStickY;
    }

    public float getRightStickX() {
        return rightStickX;
    }

    public void setRightStickX(float rightStickX) {
        this.rightStickX = rightStickX;
    }

    public float getRightStickY() {
        return rightStickY;
    }

    public void setRightStickY(float rightStickY) {
        this.rightStickY = rightStickY;
    }

    public float getLeftShoulderTrigger() {
        return leftShoulderTrigger;
    }

    public void setLeftShoulderTrigger(float leftShoulderTrigger) {
        this.leftShoulderTrigger = leftShoulderTrigger;
    }

    public float getRightShoulderTrigger() {
        return rightShoulderTrigger;
    }

    public void setRightShoulderTrigger(float rightShoulderTrigger) {
        this.rightShoulderTrigger = rightShoulderTrigger;
    }
}
