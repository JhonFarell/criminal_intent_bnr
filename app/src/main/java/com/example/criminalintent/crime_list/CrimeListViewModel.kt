package com.example.criminalintent.crime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.database.CrimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    val crimes = mutableListOf<CrimeModel>()

    init {
        viewModelScope.launch {
            delay(5000)
            crimes += loadCrimes()
        }
    }
    suspend fun loadCrimes(): List<CrimeModel>{

        return crimeRepository.getCrimes()
    }
}
