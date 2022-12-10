package com.example.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.CrimeModel

@Database (entities = [CrimeModel::class], version = 4)
@TypeConverters(CrimeTypeConverter::class)
abstract class CrimeDatabase: RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}