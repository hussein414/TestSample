package com.example.testsample.ui.viewmodel.policy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testsample.data.model.PolicyModel
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.data.repository.PolicyRepository
import com.example.testsample.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PolicyViewModel(app: Application, private val repository: PolicyRepository) :
    AndroidViewModel(app) {
    fun addPolicy(policyModel: PolicyModel) = viewModelScope.launch {
        repository.addPolicy(policyModel)
    }

    fun deletePolicy(policyModel: PolicyModel) = viewModelScope.launch {
        repository.deletePolicy(policyModel)
    }

    fun updatePolicy(policyModel: PolicyModel) = viewModelScope.launch {
        repository.updatePolicy(policyModel)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun getAllPolicy() = repository.getAllPolicy()
}