package com.optimus.simpletimer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_timer_dialog.*


class TimerDialogFragment : DialogFragment() {

    private var secondsValue: Int = 0
    private var minutesValue: Int = 0
    private var hoursValue: Int = 0

    companion object{
        const val TAG = "TimerDialogFragment"
    }

     private var listener: OnTimeChangeListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTimeChangeListener) {
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

        np_hours.setOnValueChangedListener { _, _, newVal ->
            hoursValue = newVal
        }

        np_minutes.setOnValueChangedListener { _, _, newVal ->
            minutesValue = newVal
        }

        np_seconds.setOnValueChangedListener { _, _, newVal ->
            secondsValue = newVal
        }


        btn_dialog_timer_submit.setOnClickListener {
            listener?.onTimeChange(hoursValue, minutesValue, secondsValue)
            Log.e("M_TimerDialogFragment", "onTimeChange: $hoursValue, $minutesValue, $secondsValue")
            dismiss()
        }

        btn_dialog_timer_cancel.setOnClickListener {
            dismiss()
        }


    }



    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnTimeChangeListener {
        fun onTimeChange(hours: Int, minutes: Int, seconds: Int)
    }


}
