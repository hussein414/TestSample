package com.example.testsample.data.repository

import com.example.testsample.data.db.ProfileDatabase
import com.example.testsample.data.model.ProfileModel

class ProfileRepository(private val profileDatabase: ProfileDatabase) {
    suspend fun addProfile(note: ProfileModel) = profileDatabase.getProfileDao().insertProfile(note)
    suspend fun deleteProfile(note: ProfileModel) =
        profileDatabase.getProfileDao().deleteProfile(note)

    suspend fun updateProfile(note: ProfileModel) =
        profileDatabase.getProfileDao().updateProfile(note)

    fun getAllProfile() = profileDatabase.getProfileDao().getAllNotes()
    suspend fun isDuplicate(stringValue: String): Boolean {
        val duplicateCount = profileDatabase.getProfileDao().checkDuplicate(stringValue)
        return duplicateCount > 0
    }
}