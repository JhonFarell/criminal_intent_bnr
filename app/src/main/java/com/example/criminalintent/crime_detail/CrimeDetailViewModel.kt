package com.example.criminalintent.crime_detail

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
import java.text.DateFormat
import java.util.*

class CrimeDetailViewModel(crimeId: UUID): ViewModel () {
    private var crimeRepository = CrimeRepository.get()
    private val _crime: MutableStateFlow<CrimeModel?> = MutableStateFlow(null)
    val crime: StateFlow<CrimeModel?> = _crime.asStateFlow()
    var photoName: String? = null

    //Gets crime data from DB
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

    fun timeFormat(date: Date): String {
        val timeFormat: DateFormat = when (Locale.getDefault().language) {
        "en" -> {
            DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH)}
        else -> {
            DateFormat.getTimeInstance(DateFormat.SHORT, Locale("es", "ES"))
        } }
        return timeFormat.format(date)
    }

    fun dateFormat(dateInput: Date): String {
        val dateFormat: DateFormat = when (Locale.getDefault().language) {
            "en" -> {
                DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH)
            }
            else -> {
                DateFormat.getDateInstance(DateFormat.MEDIUM, Locale("es", "ES"))
            }
        }

        return dateFormat.format(dateInput)
    }

    suspend fun deleteCrime() {
        crime.value?.let {crimeRepository.deleteCrime(it)}
    }


}

class CrimeDetailViewModelFactory (
    private val crimeId: UUID
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
        }