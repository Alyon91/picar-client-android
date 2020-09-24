package com.rpinferetti.picar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.rpinferetti.gamepad.Gamepad

import com.rpinferetti.picar.ui.ConnectFragment

class MainActivity : AppCompatActivity(),
        ConnectFragment.OnConnectListener {

    private lateinit var mNavController: NavController

    private var mSocket: MySocket? = null
    private var mAddress: String? = null
    private var mPort: Int = 0

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private lateinit var mVehicle: Vehicle
    private lateinit var mGamepad: Gamepad
    private lateinit var mGamepadControl: GamepadControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        mVehicle = Vehicle()
        mGamepad = Gamepad()
        mGamepadControl = GamepadControl(mVehicle)

        mHandler = Handler(Looper.getMainLooper())
        mRunnable = object : Runnable {
            override fun run() {
                mGamepadControl.handleInput(mGamepad)
                mHandler.postDelayed(this, 100)
            }
        }
    }

    private fun startGamepadPolling() {
        mHandler.post(mRunnable)
    }

    private fun stopGamepadPolling() {
        mHandler.removeCallbacks(mRunnable)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (mGamepad.shouldHandleKeyEvent(event)) {
            mGamepad.handleKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (mGamepad.shouldHandleMotionEvent(event)) {
            mGamepad.handleMotionEvent(event)
            return true
        }
        return super.dispatchGenericMotionEvent(event)
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show() }
    }

    override fun onConnectButtonPressed(address: String?, port: Int, sockType: Int) {
        mSocket?.disconnect()

        if (address != null && address.isNotEmpty() && port > 0 && port < 65535) {
            when (sockType) {
                ConnectFragment.SOCK_TCP -> mSocket = TCPSocket(address, port)
                ConnectFragment.SOCK_UDP -> mSocket = UDPSocket(address, port)
            }

            mSocket?.onMySocketListener = object : MySocket.OnMySocketListener {
                override fun onConnectSuccess() {
                    showToast("Connesso")

                    mSocket?.let { it ->
                        mVehicle.setSocket(it)
                        val action = R.id.action_connectFragment_to_controlFragment
                        val bundle = bundleOf("vehicle" to mVehicle)
                        mNavController.navigate(action, bundle)
                    }
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
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}