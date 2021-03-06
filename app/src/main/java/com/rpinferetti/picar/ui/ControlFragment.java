package com.rpinferetti.picar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.rpinferetti.picar.R;


public class ControlFragment extends Fragment {

    private OnControlListener mListener;

    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ControlFragment.
     */
    static ControlFragment newInstance() {
        return new ControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        initButtons(view);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initButtons(View view) {
        ImageButton forwardButton = view.findViewById(R.id.btn_forward);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.onControlButtonPressed(OnControlListener.FORWARD);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mListener.onControlButtonPressed(OnControlListener.STOP);
                    return true;
                }
                return false;
            }
        });

        ImageButton backwardButton = view.findViewById(R.id.btn_backward);
        backwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.onControlButtonPressed(OnControlListener.BACKWARD);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mListener.onControlButtonPressed(OnControlListener.STOP);
                    return true;
                }
                return false;
            }
        });

        ImageButton turnLeftButton = view.findViewById(R.id.btn_turn_left);
        turnLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.onControlButtonPressed(OnControlListener.LEFT);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mListener.onControlButtonPressed(OnControlListener.CENTER);
                    return true;
                }
                return false;
            }
        });

        ImageButton turnRightButton = view.findViewById(R.id.btn_turn_right);
        turnRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.onControlButtonPressed(OnControlListener.RIGHT);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mListener.onControlButtonPressed(OnControlListener.CENTER);
                    return true;
                }
                return false;
            }
        });

        ImageButton blueButton = view.findViewById(R.id.btn_rgb_blue);
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onControlButtonPressed(OnControlListener.RGB_BLUE);
            }
        });

        ImageButton redButton = view.findViewById(R.id.btn_rgb_red);
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onControlButtonPressed(OnControlListener.RGB_RED);
            }
        });

        ImageButton greenButton = view.findViewById(R.id.btn_rgb_green);
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onControlButtonPressed(OnControlListener.RGB_GREEN);
            }
        });

        ImageButton buzzerButton = view.findViewById(R.id.btn_buzzer);
        buzzerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mListener.onControlButtonPressed(OnControlListener.BUZZER);
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mListener.onControlButtonPressed(OnControlListener.BUZZER);
                    return true;
                }
                return false;
            }
        });

        ImageButton cameraButton = view.findViewById(R.id.btn_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onControlButtonPressed(OnControlListener.CAMERA);
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnControlListener) {
            mListener = (OnControlListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnControlListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnControlListener {
        int STOP = 0;
        int FORWARD = 1;
        int BACKWARD = 2;
        int LEFT = 3;
        int RIGHT = 4;
        int CENTER = 5;
        int RGB_BLUE = 6;
        int RGB_RED = 7;
        int RGB_GREEN = 8;
        int BUZZER = 9;
        int CAMERA = 10;

        void onControlButtonPressed(int button);
    }
}
