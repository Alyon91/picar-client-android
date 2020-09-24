package com.rpinferetti.picar.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.rpinferetti.picar.R
import com.rpinferetti.picar.Vehicle

class ControlFragment : Fragment() {

    private var vehicle: Vehicle? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_control, container, false)

        vehicle = arguments?.getParcelable("vehicle")

        initButtons(view)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initButtons(view: View) {
        val forwardButton = view.findViewById<ImageButton>(R.id.btn_forward)
        forwardButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                vehicle?.moveForward(100)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                vehicle?.moveStop()
                return@OnTouchListener true
            }
            false
        })

        val backwardButton = view.findViewById<ImageButton>(R.id.btn_backward)
        backwardButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                vehicle?.moveBackward(100)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                vehicle?.moveStop()
                return@OnTouchListener true
            }
            false
        })

        val turnLeftButton = view.findViewById<ImageButton>(R.id.btn_turn_left)
        turnLeftButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                vehicle?.turnLeft(100)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                vehicle?.turnCenter()
                return@OnTouchListener true
            }
            false
        })

        val turnRightButton = view.findViewById<ImageButton>(R.id.btn_turn_right)
        turnRightButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                vehicle?.turnRight(100)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                vehicle?.turnCenter()
                return@OnTouchListener true
            }
            false
        })

        val blueButton = view.findViewById<ImageButton>(R.id.btn_rgb_blue)
        blueButton.setOnClickListener {
            vehicle?.rgbBlue()
        }

        val redButton = view.findViewById<ImageButton>(R.id.btn_rgb_red)
        redButton.setOnClickListener {
            vehicle?.rgbRed()
        }

        val greenButton = view.findViewById<ImageButton>(R.id.btn_rgb_green)
        greenButton.setOnClickListener {
            vehicle?.rgbGreen()
        }

        val buzzerButton = view.findViewById<ImageButton>(R.id.btn_buzzer)
        buzzerButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                vehicle?.buzzer()
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                vehicle?.buzzer()
                return@OnTouchListener true
            }
            false
        })

        val cameraButton = view.findViewById<ImageButton>(R.id.btn_camera)
        cameraButton.setOnClickListener {
            //mListener.onControlButtonPressed(OnControlListener.CAMERA)
        }
    }


    companion object {
        /**
         * @return A new instance of fragment ControlFragment.
         */
        fun newInstance(): ControlFragment {
            return ControlFragment()
        }
    }
}