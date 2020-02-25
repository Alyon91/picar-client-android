package com.rpinferetti.picar.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.VideoView

import com.rpinferetti.picar.R

private const val URL_PARAM = "URL"

class VideoPlayerFragment : Fragment() {
    private var url: String = ""
    private var videoView: VideoView? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(URL_PARAM, "")
            Log.d("VideoPlayerFragment", url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_video_player, container, false)

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        videoView = root.findViewById(R.id.video_view)

        videoView?.apply {
            setVideoURI(Uri.parse(url))
            requestFocus()
            start()
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
        */
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(URL_PARAM, url)
                }
            }
    }
}
