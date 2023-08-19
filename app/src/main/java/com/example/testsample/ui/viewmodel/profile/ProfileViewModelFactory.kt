package com.example.testsample.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testsample.data.repository.ProfileRepository

class ProfileViewModelFactory (val app: Application, private val repository: ProfileRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(app, repository) as T
    }
}