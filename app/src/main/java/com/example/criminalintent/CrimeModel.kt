package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.text.DateFormat
import java.util.*

@Entity (tableName = "crime")
data class CrimeModel(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val isCriminal: Boolean,
    val suspect: String = "",
    val isNew: Boolean,
    val image: String? = null
)