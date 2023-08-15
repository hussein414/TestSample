package com.example.testsample.ui.view.activites

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import com.example.testsample.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import com.example.testsample.R
import com.example.testsample.data.db.ProfileDatabase
import com.example.testsample.data.repository.ProfileRepository
import com.example.testsample.ui.viewmodel.ProfileViewModel
import com.example.testsample.ui.viewmodel.ProfileViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBar()

        setViewModel()
    }

    private fun setStatusBar() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.main_color)
    }
    private fun setViewModel() {
        val profileRepository = ProfileRepository(
            ProfileDatabase(applicationContext)
        )

        val viewModelProviderFactory = ProfileViewModelFactory(
            application, profileRepository
        )

        profileViewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[ProfileViewModel::class.java]
    }

}