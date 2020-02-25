package com.rpinferetti.picar.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.rpinferetti.picar.R

class ConnectFragment : Fragment() {
    private var mSockType = 1
    private var mListener: OnConnectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        // IP ADDRESS and PORT inputs
        val addressInput: TextInputEditText = view.findViewById(R.id.ipaddress_input)
        val portInput: TextInputEditText = view.findViewById(R.id.port_input)
        addressInput.setText(SERVER_IP)
        portInput.setText(SERVER_PORT.toString())

        // Socket type radio button
        val radioGroup = view.findViewById<RadioGroup>(R.id.sock_radio_group)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.sock_radio_tcp -> mSockType = SOCK_TCP
                R.id.sock_radio_udp -> mSockType = SOCK_UDP
            }
        }

        // Connect button
        val connectButton = view.findViewById<Button>(R.id.connect_button)
        connectButton.setOnClickListener {
            val addr = addressInput.text.toString()
            val port = portInput.text.toString()
            mListener?.onConnectButtonPressed(addr, port.toInt(), mSockType)
        }
        // Force portrait orientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnConnectListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnConnectListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnConnectListener {
        fun onConnectButtonPressed(
            address: String?,
            port: Int,
            sockType: Int
        )
    }

    companion object {
        private const val SERVER_IP = "192.168.4.1"
        private const val SERVER_PORT = 12345
        const val SOCK_TCP = 0
        const val SOCK_UDP = 1

        /**
         * @return A new instance of fragment ConnectFragment.
         */
        fun newInstance(): ConnectFragment {
            return ConnectFragment()
        }
    }
}