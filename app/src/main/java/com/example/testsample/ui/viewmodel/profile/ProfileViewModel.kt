package com.example.testsample.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testsample.data.model.ProfileModel
import com.example.testsample.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


      fun checkDuplicate(stringValue: String): LiveData<Boolean> {
          val resultLiveData = MutableLiveData<Boolean>()
          viewModelScope.launch {
              val isDuplicate = withContext(Dispatchers.IO) {
                  repository.isDuplicate(stringValue)
              }
              resultLiveData.postValue(isDuplicate)
          }

          return resultLiveData
      }
    fun getAllProfile() = repository.getAllProfile()
}