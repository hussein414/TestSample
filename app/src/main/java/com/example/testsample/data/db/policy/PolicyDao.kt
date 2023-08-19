package com.example.testsample.data.db.policy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel

@Dao
interface PolicyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicy(policyModel: PolicyModel)

    @Update
    suspend fun updatePolicy(policyModel: PolicyModel)

    @Delete
    suspend fun deletePolicy(policyModel: PolicyModel)


    @Query("DELETE FROM PolicyModel")
    suspend fun deleteAll()


    @Query("SELECT * FROM PolicyModel ORDER BY id DESC")
    fun getAllPolicy(): LiveData<List<PolicyModel>>
}