package com.rpinferetti.picar;

public interface Command {

    String MOVE_FORWARD = ">Move Forward";
    String MOVE_BACKWARD = ">Move Backward";
    String TURN_LEFT = ">Turn Left";
    String TURN_RIGHT = ">Turn Right";
    String STOP = ">Move Stop";
    String TURN_CENTER = ">Turn Center90";
    String CAMERA_UP = ">Camera Up";
    String CAMERA_DOWN = ">Camera Down";
    String CAMERA_LEFT = ">Camera Left";
    String CAMERA_RIGHT = ">Camera Right";
    String CAMERA_STOP = ">Camera Stop";
    String CAMERA_CENTER = ">Camera Center";

    String SPEED_SLIDER = ">Speed Slider";
    String DIR_SLIDER = ">Direction Slider";
    String CAMERA_SLIDER = ">Camera Slider";

    String RGB_RED = ">RGB Red";
    String RGB_GREEN = ">RGB Green";
    String RGB_BLUE = ">RGB Blue";

    String BUZZER_ALARM = ">Buzzer Alarm";
    String ULTRASONIC = ">Ultrasonic";
    String SONIC_LEFT = CAMERA_LEFT;
    String SONIC_RIGHT = CAMERA_RIGHT;
}
