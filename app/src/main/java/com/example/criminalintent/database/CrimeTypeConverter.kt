package com.example.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

class CrimeTypeConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}