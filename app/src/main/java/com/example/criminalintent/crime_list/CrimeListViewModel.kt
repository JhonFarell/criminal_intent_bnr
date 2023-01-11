package com.example.criminalintent.crime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.database.CrimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class CrimeListViewModel: ViewModel() {
    private val crimeRepository = CrimeRepository.get()
    private val crimes = mutableListOf<CrimeModel>()

    init {
        viewModelScope.launch {
            crimes += loadCrimes()
        }
    }
    suspend fun loadCrimes(): List<CrimeModel>{

        return crimeRepository.getCrimes()
    }

    suspend fun addCrime(crime: CrimeModel) {
            crimeRepository.addCrime(crime)
    }
}
