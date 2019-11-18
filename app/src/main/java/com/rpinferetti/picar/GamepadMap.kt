package com.rpinferetti.picar

class GamepadMap {
    var isButtonX: Boolean = false
    var isButtonA: Boolean = false
    var isButtonY: Boolean = false
    var isButtonB: Boolean = false
    var isButtonR1: Boolean = false
    var isButtonL1: Boolean = false

    var leftStickX: Float = 0.toFloat()
    var leftStickY: Float = 0.toFloat()

    var rightStickX: Float = 0.toFloat()
    var rightStickY: Float = 0.toFloat()

    var leftShoulderTrigger: Float = 0.toFloat()
    var rightShoulderTrigger: Float = 0.toFloat()
}
