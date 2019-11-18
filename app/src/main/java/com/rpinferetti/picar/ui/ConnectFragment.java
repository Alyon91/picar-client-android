package com.rpinferetti.picar.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.rpinferetti.picar.R;


public class ConnectFragment extends Fragment {

    private static final String TAG = "ConnectFragment";

    private static final String SERVER_IP = "192.168.4.1";
    private static final int SERVER_PORT = 12345;

    public static final int SOCK_TCP = 0;
    public static final int SOCK_UDP = 1;

    private int mSockType = 1;

    private OnConnectListener mListener;

    public ConnectFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ConnectFragment.
     */
    static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        // IP ADDRESS and PORT inputs
        final TextInputEditText addressInput = view.findViewById(R.id.ipaddress_input);
        final TextInputEditText portInput = view.findViewById(R.id.port_input);
        addressInput.setText(SERVER_IP);
        portInput.setText(String.valueOf(SERVER_PORT));

        // Socket type radio button
        RadioGroup radioGroup = view.findViewById(R.id.sock_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.sock_radio_tcp:
                        mSockType = SOCK_TCP;
                        break;

                    case R.id.sock_radio_udp:
                        mSockType = SOCK_UDP;
                        break;
                }
            }
        });

        // Connect button
        Button connectButton = view.findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addr = addressInput.getText().toString();
                String port = portInput.getText().toString();
                mListener.onConnectButtonPressed(addr, Integer.parseInt(port), mSockType);
            }
        });

        // Force portrait orientation
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectListener) {
            mListener = (OnConnectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConnectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnConnectListener {
        void onConnectButtonPressed(String address, int port, int sockType);
    }

}
