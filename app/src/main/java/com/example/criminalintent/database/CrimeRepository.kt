package com.example.criminalintent.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminalintent.CrimeModel
import com.example.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "crime-database"



class CrimeRepository private constructor(context: Context,
                                          private val coroutineScope: CoroutineScope = GlobalScope) {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `crime` ADD COLUMN isCriminal INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `crime` ADD COLUMN suspect TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3,4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `crime` ADD COLUMN isNew INTEGER NOT NULL DEFAULT 1")
        }
    }

    private val MIGRATION_4_5 = object: Migration(4,5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `crime` ADD COLUMN image TEXT")
        }
    }

    private val database:CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
        .build()


    suspend fun getCrimes(): List<CrimeModel> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): CrimeModel = database.crimeDao().getCrime(id)
    suspend fun addCrime(crime: CrimeModel) = database.crimeDao().addCrime(crime)
    suspend fun deleteCrime(crime: CrimeModel) = database.crimeDao().deleteCrime(crime)

    fun updateCrime(crime: CrimeModel) {
       coroutineScope.launch {database.crimeDao().updateCrime(crime)}
    }



    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize (context: Context) {
            if (INSTANCE==null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("Crime repository must be initialized")
        }
    }
}