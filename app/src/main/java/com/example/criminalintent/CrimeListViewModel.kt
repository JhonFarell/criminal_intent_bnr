package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeListViewModel: ViewModel() {
    val crimes = mutableListOf<CrimeModel>()

    init {
        for (i in 0 until 100) {
                val crime = CrimeModel(
                    id = UUID.randomUUID(),
                    title = "Crime №$i",
                    date = Date(),
                    isSolved = i % 2 == 0,
                    isCriminal = i % 3 == 0)

            crimes += crime
        }
    }
}