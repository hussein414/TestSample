package com.example.testsample.data.db.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.testsample.data.model.ProfileModel

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileModel: ProfileModel)

    @Update
    suspend fun updateProfile(profileModel: ProfileModel)

    @Delete
    suspend fun deleteProfile(profileModel: ProfileModel)


    @Query("SELECT * FROM ProfileModel ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<ProfileModel>>


    @Query("SELECT COUNT(*) FROM ProfileModel WHERE name = :stringValue")
    suspend fun checkDuplicate(stringValue: String): Int
}