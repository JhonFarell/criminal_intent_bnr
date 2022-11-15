package com.example.criminalintent.crime_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.database.CrimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class CrimeDetailViewModel(crimeId: UUID): ViewModel () {
    private var crimeRepository = CrimeRepository.get()
    private val _crime: MutableStateFlow<CrimeModel?> = MutableStateFlow(null)
    val crime: StateFlow<CrimeModel?> = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }

    fun updateCrime(onUpdate: (CrimeModel) -> CrimeModel) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    fun updateDbValue () {
        crime.value?.let {crimeRepository.updateCrime(it)}
    }

}

class CrimeDetailViewModelFactory (
    private val crimeId: UUID
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
        }