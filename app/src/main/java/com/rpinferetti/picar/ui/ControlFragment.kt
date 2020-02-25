package com.rpinferetti.picar.ui

import android.annotation.SuppressLint
import android.content.Context
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

class ControlFragment : Fragment() {
    private var mListener: OnControlListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_control, container, false)
        initButtons(view)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initButtons(view: View) {
        val forwardButton = view.findViewById<ImageButton>(R.id.btn_forward)
        forwardButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                mListener?.onControlButtonPressed(OnControlListener.FORWARD)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onControlButtonPressed(OnControlListener.STOP)
                return@OnTouchListener true
            }
            false
        })

        val backwardButton = view.findViewById<ImageButton>(R.id.btn_backward)
        backwardButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                mListener?.onControlButtonPressed(OnControlListener.BACKWARD)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onControlButtonPressed(OnControlListener.STOP)
                return@OnTouchListener true
            }
            false
        })

        val turnLeftButton = view.findViewById<ImageButton>(R.id.btn_turn_left)
        turnLeftButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                mListener?.onControlButtonPressed(OnControlListener.LEFT)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onControlButtonPressed(OnControlListener.CENTER)
                return@OnTouchListener true
            }
            false
        })

        val turnRightButton = view.findViewById<ImageButton>(R.id.btn_turn_right)
        turnRightButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                mListener?.onControlButtonPressed(OnControlListener.RIGHT)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onControlButtonPressed(OnControlListener.CENTER)
                return@OnTouchListener true
            }
            false
        })

        val blueButton = view.findViewById<ImageButton>(R.id.btn_rgb_blue)
        blueButton.setOnClickListener { mListener?.onControlButtonPressed(OnControlListener.RGB_BLUE) }

        val redButton = view.findViewById<ImageButton>(R.id.btn_rgb_red)
        redButton.setOnClickListener { mListener?.onControlButtonPressed(OnControlListener.RGB_RED) }

        val greenButton = view.findViewById<ImageButton>(R.id.btn_rgb_green)
        greenButton.setOnClickListener { mListener?.onControlButtonPressed(OnControlListener.RGB_GREEN) }

        val buzzerButton = view.findViewById<ImageButton>(R.id.btn_buzzer)
        buzzerButton.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                mListener?.onControlButtonPressed(OnControlListener.BUZZER)
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mListener?.onControlButtonPressed(OnControlListener.BUZZER)
                return@OnTouchListener true
            }
            false
        })

        val cameraButton = view.findViewById<ImageButton>(R.id.btn_camera)
        cameraButton.setOnClickListener { mListener?.onControlButtonPressed(OnControlListener.CAMERA) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnControlListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnControlListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnControlListener {
        fun onControlButtonPressed(button: Int)

        companion object {
            const val STOP = 0
            const val FORWARD = 1
            const val BACKWARD = 2
            const val LEFT = 3
            const val RIGHT = 4
            const val CENTER = 5
            const val RGB_BLUE = 6
            const val RGB_RED = 7
            const val RGB_GREEN = 8
            const val BUZZER = 9
            const val CAMERA = 10
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