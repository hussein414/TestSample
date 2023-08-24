package com.example.testsample.ui.activites

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

import com.example.testsample.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import com.example.testsample.R
import com.example.testsample.data.db.policy.PolicyDatabase
import com.example.testsample.data.db.profile.ProfileDatabase
import com.example.testsample.data.repository.PolicyRepository
import com.example.testsample.data.repository.ProfileRepository
import com.example.testsample.ui.viewmodel.policy.PolicyViewModel
import com.example.testsample.ui.viewmodel.policy.PolicyViewModelFactory
import com.example.testsample.ui.viewmodel.profile.ProfileViewModel
import com.example.testsample.ui.viewmodel.profile.ProfileViewModelFactory
import com.example.testsample.utils.VpnCaller
import com.example.testsample.vpnclient.vpn.Constance
import com.example.testsample.vpnclient.vpn.tlsVPNService
import java.io.File
import java.util.UUID
import kotlin.jvm.internal.Intrinsics.Kotlin


class MainActivity : AppCompatActivity() {
    //NEW-CHANGES

    //NEW-CHANGES

    private lateinit var binding: ActivityMainBinding
    lateinit var profileViewModel: ProfileViewModel
    lateinit var policyViewModel: PolicyViewModel
    lateinit var vpnCaller: VpnCaller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBar()
        setProfileFactoryViewModel()
        setPolicyFactoryViewModel()
        prepareVpn()
    }

    class VpnContent : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

    private val vpnContent = registerForActivityResult(VpnContent()) {
        if (it) {
          Constance.vpnPermission = true
        }
    }

    private fun prepareVpn() {
        VpnService.prepare(this@MainActivity)?.let {
            vpnContent.launch(it)
        }
            ?: kotlin.run {
                Constance.vpnPermission = true
            }
    }

    private fun setStatusBar() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.main_color)
    }

    private fun setProfileFactoryViewModel() {
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


    private fun setPolicyFactoryViewModel() {
        val policyRepository = PolicyRepository(
            PolicyDatabase(applicationContext)
        )

        val viewModelProviderFactory = PolicyViewModelFactory(
            application, policyRepository
        )

        policyViewModel = ViewModelProvider(
            this, viewModelProviderFactory
        )[PolicyViewModel::class.java]
    }
}