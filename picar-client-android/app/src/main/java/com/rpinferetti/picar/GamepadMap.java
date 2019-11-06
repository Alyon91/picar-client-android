package com.rpinferetti.picar;

public class GamepadMap {
    private boolean buttonX;
    private boolean buttonA;
    private boolean buttonY;
    private boolean buttonB;
    private boolean buttonR1;
    private boolean buttonL1;

    public boolean isButtonR1() {
        return buttonR1;
    }

    public void setButtonR1(boolean buttonR1) {
        this.buttonR1 = buttonR1;
    }

    public boolean isButtonL1() {
        return buttonL1;
    }

    public void setButtonL1(boolean buttonL1) {
        this.buttonL1 = buttonL1;
    }

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

    public boolean isButtonX() {
        return buttonX;
    }

    public void setButtonX(boolean buttonX) {
        this.buttonX = buttonX;
    }

    public boolean isButtonA() {
        return buttonA;
    }

    public void setButtonA(boolean buttonA) {
        this.buttonA = buttonA;
    }

    public boolean isButtonY() {
        return buttonY;
    }

    public void setButtonY(boolean buttonY) {
        this.buttonY = buttonY;
    }

    public boolean isButtonB() {
        return buttonB;
    }

    public void setButtonB(boolean buttonB) {
        this.buttonB = buttonB;
    }
}
