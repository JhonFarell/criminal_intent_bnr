package com.example.criminalintent.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs

class DatePickerFragment: DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val cal = Calendar.getInstance()
        cal.time = args.crimeDate

        val onDateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->

            val hour = cal.get(Calendar.HOUR)
            val minute = cal.get(Calendar.MINUTE)
            val resultDate = GregorianCalendar(year, month, day, hour, minute).time

            setFragmentResult(REQUEST_KEY_DATE,
                                bundleOf(BUNDLE_KEY_DATE to resultDate)
            )
        }

        val initialYear = cal.get(Calendar.YEAR)
        val initialMonth = cal.get(Calendar.MONTH)
        val initialDay = cal.get(Calendar.DAY_OF_MONTH)


        return DatePickerDialog(
            requireContext(),
            onDateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }
}

