package com.example.testsample.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application, private val repository: ProfileRepository) :
    AndroidViewModel(app) {
    fun addProfile(note: ProfileModel) = viewModelScope.launch {
        repository.addProfile(note)
    }

    fun deleteProfile(note: ProfileModel) = viewModelScope.launch {
        repository.deleteProfile(note)
    }

    fun updateProfile(note: ProfileModel) = viewModelScope.launch {
        repository.updateProfile(note)
    }

    fun getAllProfile() = repository.getAllProfile()
}