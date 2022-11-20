package com.example.criminalintent.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar

class TimePickerFragment: DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val cal = Calendar.getInstance()
        cal.time = args.crimeTime

        val onTimeListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val returnTime = GregorianCalendar(year,month,day,hour, minute).time

            setFragmentResult(REQUEST_KEY_TIME,
                                bundleOf(BUNDlE_KEY_TIME to returnTime))
        }


        val initialHour = cal.get(Calendar.HOUR_OF_DAY)
        val initialMinute = cal.get(Calendar.MINUTE)
        val is24HourListener = true

        return TimePickerDialog(
            requireContext(),
            onTimeListener,
            initialHour,
            initialMinute,
            is24HourListener
        )
    }
    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDlE_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}