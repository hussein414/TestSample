package com.example.testsample.ui.activites

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
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
import com.example.testsample.vpnclient.vpn.tlsVPNService
import java.io.File
import java.util.UUID


class MainActivity : AppCompatActivity() {
    //NEW-CHANGES
    fun getUUID():String {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId != null && androidId.isNotEmpty()) {
            println("ANDROID_ID: $androidId")
            return UUID.nameUUIDFromBytes(androidId.toByteArray()).toString()
        } else {
            return UUID.randomUUID().toString()
        }
    }
    var myStart:Boolean=false;
    private lateinit var editText: EditText
    fun startVpn(){
        startService(Intent(this@MainActivity, tlsVPNService::class.java).also {
            it.putExtra("CONFIG_NAME",save_config())
            it.putExtra("UUID",getUUID())
            it.action = tlsVPNService.ACTION_CONNECT })
    }

    private fun stopVpn() {
        startService(Intent(this@MainActivity, tlsVPNService::class.java).also { it.action = tlsVPNService.ACTION_DISCONNECT })
    }

    private fun load_config(){
        val filename = "tlsTunnel.cfg"
        val file = File(this.filesDir, filename)
        if (file.canRead()) {
            val config = file.readText()
            if (config.isEmpty() == false) editText.setText(config)
        }
    }

    private fun save_config():String{
        val filename = "tlsTunnel.cfg"
        val file = File(this.filesDir, filename)
        file.writeText(editText.text.toString())
        return file.absolutePath
    }
    private fun setupUI(isStart: Boolean? = null) {
        /*        if (isStart == true) {
                    findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.stop_vpn)
                } else if (isStart == false) {
                    findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.start_vpn)
                } else {
                    if (isMyVpnServiceRunning) {
                        findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.stop_vpn)
                    } else {
                        findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.start_vpn)
                    }
                }*/
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
            startVpn()
        }
    }

    private fun prepareVpn() {
        VpnService.prepare(this@MainActivity)?.let {
            vpnContent.launch(it)
        } ?: kotlin.run {
            startVpn()
        }
    }
    private fun vpnOnCreate(){
        //setContentView(R.layout.activity_main)

        // setupUI()
        //val btnStartVPN = findViewById<Button>(R.id.btnStartVPN)

        //editText = findViewById(R.id.config)
        //load_config();

        /*btnStartVPN.setOnClickListener {
            if (isMyVpnServiceRunning) {
                stopVpn()
                setupUI(isStart = false)
            } else {
                prepareVpn()
                setupUI(isStart = true)
            }
        }*/
    }
    //NEW-CHANGES

    private lateinit var binding: ActivityMainBinding
    lateinit var profileViewModel: ProfileViewModel
    lateinit var policyViewModel: PolicyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBar()

        setProfileFactoryViewModel()
        setPolicyFactoryViewModel()
        vpnOnCreate()
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