package com.example.criminalintent

import java.text.DateFormat
import java.util.*

data class CrimeModel(
    val id: UUID,
    val title: String,
    val date: String,
    val isSolved: Boolean,
    val isCriminal: Boolean
)