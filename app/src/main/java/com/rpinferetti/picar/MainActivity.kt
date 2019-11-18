package com.rpinferetti.picar

import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.rpinferetti.picar.ui.ConnectFragment
import com.rpinferetti.picar.ui.ControlFragment
import kotlin.math.abs
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), ControlFragment.OnControlListener,
        ConnectFragment.OnConnectListener {

    private var mNavController: NavController? = null

    private var mSocket: MySocket? = null
    private var mAddress: String? = null
    private var mPort: Int = 0

    private var mHandler: Handler? = null
    private var mGamepadRunnable: Runnable? = null

    private var mVehicle: Vehicle? = null
    private var mGamepad: Gamepad? = null
    private var mGamepadMap: GamepadMap? = null

    private var isButtonYPressed = false
    private var isStopped = true
    private var isCentered = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        mGamepad = Gamepad()
        mGamepad?.setOnGamepadListener(object : Gamepad.OnGamepadListener {
            override fun onGamepadMapChanged(map: GamepadMap) {
                mGamepadMap = map
            }
        })
    }

    private fun startGamepadPolling() {
        mHandler = Handler()
        mGamepadRunnable = object : Runnable {
            override fun run() {
                try {
                    mGamepadMap?.let {
                        mVehicle?.apply {
                            if (it.leftStickX == 0f && !isCentered) {
                                isCentered = true
                                turnCenter()
                                Thread.sleep(10)
                            }

                            if (mGamepadMap!!.leftStickX > 0) {
                                isCentered = false
                                val speed = it.leftStickX.roundToInt()
                                turnRight(speed)
                                Thread.sleep(10)
                            }

                            if (it.leftStickX < 0) {
                                isCentered = false
                                val speed = it.leftStickX.roundToInt()
                                turnLeft(abs(speed))
                                Thread.sleep(10)
                            }

                            if (it.rightShoulderTrigger == 0f &&
                                    it.leftShoulderTrigger == 0f && !isStopped
                            ) {
                                isStopped = true
                                moveStop()
                                Thread.sleep(10)
                            }

                            if (it.rightShoulderTrigger > 0) {
                                isStopped = false
                                val speed = it.rightShoulderTrigger.roundToInt()
                                moveForward(speed)
                                Thread.sleep(10)
                            }

                            if (it.leftShoulderTrigger > 0) {
                                isStopped = false
                                val speed = it.leftShoulderTrigger.roundToInt()
                                moveBackward(speed)
                                Thread.sleep(10)
                            }

                            if (it.isButtonX) {
                                rgbBlue()
                                Thread.sleep(10)
                            }

                            if (it.isButtonA) {
                                rgbGreen()
                                Thread.sleep(10)
                            }

                            if (it.isButtonB) {
                                rgbRed()
                                Thread.sleep(10)
                            }

                            if (it.isButtonY && !isButtonYPressed) {
                                isButtonYPressed = true
                                buzzer()
                                Thread.sleep(10)
                            }

                            if (!it.isButtonY && isButtonYPressed) {
                                isButtonYPressed = false
                                buzzer()
                                Thread.sleep(10)
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    mHandler?.postDelayed(this, 80)
                }
            }
        }

        mGamepadRunnable?.run()
    }

    private fun stopGamepadPolling() {
        mGamepadRunnable?.let { mHandler?.removeCallbacks(it) }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                mGamepad?.handleButtonPressed(event)
                return true
            }
        }

        return super.dispatchKeyEvent(event)
    }


    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        // Check that the event came from a game controller
        if (event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE) {

            // Process all historical movement samples in the batch
            val historySize = event.historySize

            // Process the movements starting from the
            // earliest historical position in the batch
            for (i in 0 until historySize) {
                // Process the event at historical position i
                mGamepad?.processJoystickInput(event, i)
            }

            // Process the current movement sample in the batch (position -1)
            mGamepad?.processJoystickInput(event, -1)
            return true
        }

        return super.dispatchGenericMotionEvent(event)
    }


    override fun onControlButtonPressed(button: Int) {
        when (button) {
            ControlFragment.OnControlListener.STOP -> mVehicle?.moveStop()

            ControlFragment.OnControlListener.FORWARD -> mVehicle?.moveForward(MOVE_SPEED)

            ControlFragment.OnControlListener.BACKWARD -> mVehicle?.moveBackward(MOVE_SPEED)

            ControlFragment.OnControlListener.LEFT -> mVehicle?.turnLeft(TURN_SPEED)

            ControlFragment.OnControlListener.RIGHT -> mVehicle?.turnRight(TURN_SPEED)

            ControlFragment.OnControlListener.CENTER -> mVehicle?.turnCenter()

            ControlFragment.OnControlListener.RGB_BLUE -> mVehicle?.rgbBlue()

            ControlFragment.OnControlListener.RGB_GREEN -> mVehicle?.rgbGreen()

            ControlFragment.OnControlListener.RGB_RED -> mVehicle?.rgbRed()

            ControlFragment.OnControlListener.BUZZER -> mVehicle?.buzzer()

            ControlFragment.OnControlListener.CAMERA -> {
                val bundle = Bundle()
                bundle.putString("URL", "http://$mAddress:8090/?action=stream")
                mNavController?.navigate(
                        R.id.action_controlFragment_to_videoPlayerFragment,
                        bundle
                )
            }

            else -> showToast("Invalid command")
        }
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show() }
    }

    override fun onConnectButtonPressed(address: String?, port: Int, sockType: Int) {
        mSocket?.disconnect()

        if (address != null && address.isNotEmpty() && port > 0 && port < 65535) {
            when (sockType) {
                ConnectFragment.SOCK_TCP -> mSocket =
                        TCPSocket(address, port)
                ConnectFragment.SOCK_UDP -> mSocket =
                        UDPSocket(address, port)
            }

            mSocket?.onMySocketListener = object : MySocket.OnMySocketListener {
                override fun onConnectSuccess() {
                    showToast("Connesso")
                    mNavController?.navigate(R.id.action_connectFragment_to_controlFragment)
                }

                override fun onConnectFailure() {
                    showToast("Impossibile connettersi")
                }

            }

            mSocket?.connect()
        }
    }

    override fun onResume() {
        super.onResume()
        startGamepadPolling()
    }

    override fun onPause() {
        super.onPause()
        stopGamepadPolling()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        outState.putString("SERVER_ADDR", mAddress)
        outState.putInt("SERVER_PORT", mPort)
        outState.putParcelable("SOCKET", mSocket)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        super.onRestoreInstanceState(savedInstanceState)
        mAddress = savedInstanceState.getString("SERVER_ADDR")
        mPort = savedInstanceState.getInt("SERVER_PORT")
        mSocket = savedInstanceState.getParcelable("SOCKET")

        mSocket?.let { mVehicle = Vehicle(it) }
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val MOVE_SPEED = 100
        private const val TURN_SPEED = 100
    }
}