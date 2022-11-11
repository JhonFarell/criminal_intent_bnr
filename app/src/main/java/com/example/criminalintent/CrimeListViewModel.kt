package com.example.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.DateFormat
import java.util.*

class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimes = mutableListOf<CrimeModel>()

    init {
        viewModelScope.launch {
            Log.d("VM", "launched")
            delay(5000)
            crimes += loadCrimes()
        }
    }
    suspend fun loadCrimes(): List<CrimeModel>{

        return crimeRepository.getCrimes()
    }
}
