package com.example.testsample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testsample.data.model.ProfileModel

@Database(entities = [ProfileModel::class], version = 1, exportSchema = false)
abstract class ProfileDatabase:RoomDatabase() {
    abstract fun getProfileDao(): ProfileDao
    companion object {
        @Volatile
        private var instance: ProfileDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?:
            buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile_db"
        ).build()
    }
}