package com.example.testsample.data.db.policy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testsample.data.model.PolicyModel

@Database(entities = [PolicyModel::class], version = 3, exportSchema = false)
abstract class PolicyDatabase : RoomDatabase() {
    abstract fun getPolicyDao(): PolicyDao

    companion object {
        @Volatile
        private var instance: PolicyDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PolicyDatabase::class.java,
            "policy_db"
        ).fallbackToDestructiveMigration().build()
    }

}