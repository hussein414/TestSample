package com.example.testsample.ui.viewmodel.policy

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testsample.data.repository.PolicyRepository
import com.example.testsample.data.repository.ProfileRepository

class PolicyViewModelFactory(val app: Application, private val repository: PolicyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PolicyViewModel(app, repository) as T
    }
}