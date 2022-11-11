package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import java.sql.Time
import java.text.DateFormat
import java.util.*

class CrimeListViewModel: ViewModel() {
    val crimes = mutableListOf<CrimeModel>()
    val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH)
    val timeFormat  = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH)
    init {
        for (i in 0 until 100) {
                val crime = CrimeModel(
                    id = UUID.randomUUID(),
                    title = "Crime №$i",
                    date = "${dateFormat.format(Date())} ${timeFormat.format(Date())}",
                    isSolved = i % 2 == 0,
                    isCriminal = i % 3 == 0)

            crimes += crime
        }
    }
}