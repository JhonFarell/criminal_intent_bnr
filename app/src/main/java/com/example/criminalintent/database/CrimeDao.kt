package com.example.criminalintent.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.criminalintent.CrimeModel
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    suspend fun getCrimes(): List<CrimeModel>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): CrimeModel

    @Update
    suspend fun updateCrime(crime: CrimeModel)

    @Insert
    suspend fun addCrime(crime: CrimeModel)

    @Delete()
    suspend fun deleteCrime(crime: CrimeModel)
}