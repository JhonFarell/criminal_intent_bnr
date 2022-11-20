package com.example.criminalintent.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import java.util.*

class TimePickDialog: DialogFragment() {

    private val args: TimePickerDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

        val onTimeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hour , minute ->
            val resultTime =


        }

        val calendar = Calendar.getInstance()
        calendar.time = args.crimeTime
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)
    }
    companion object {
        const val REQUEST_KEY_TIME = "REQUEST_KEY_TIME"
        const val BUNDle_KEY_TIME = "BUNDLE_KEY_TIME"
    }
}