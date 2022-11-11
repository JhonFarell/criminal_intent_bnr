package com.example.criminalintent

import java.util.*

data class CrimeModel(
    val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val isCriminal: Boolean
)