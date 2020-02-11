package com.optimus.simpletimer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_timer_dialog.*


class TimerDialogFragment : DialogFragment() {


    companion object{
        const val TAG = "TimerDialogFragment"
    }

     private var listener: OnFragmentInteractionListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_timer_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(np_hours) {
            minValue = 0
            maxValue = 99
        }
        with(np_minutes) {
            minValue = 0
            maxValue = 59
        }
        with(np_seconds) {
            minValue = 0
            maxValue = 59
        }


        np_hours.setOnValueChangedListener { picker, oldVal, newVal ->
//            tv_timer_value.text = newVal.toString()
//            startValue = newVal
//            progress_bar.max = newVal
        }

    }




    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
