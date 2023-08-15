package com.example.testsample.vpnclient

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.example.testsample.R
import com.fanoos.vpnclient.vpn.MyVpnService
import com.fanoos.vpnclient.vpn.isMyVpnServiceRunning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity(){
    var myStart:Boolean=false;
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        //val btnStartVPN = findViewById<Button>(R.id.btnStartVPN)

        //editText = findViewById(R.id.config)
        load_config();

/*        btnStartVPN.setOnClickListener {
            if (isMyVpnServiceRunning) {
                stopVpn()
                setupUI(isStart = false)
            } else {
                prepareVpn()
                setupUI(isStart = true)
            }
        }*/
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

     fun startVpn(V4S:String,V4L:Int){
        startService(Intent(this@MainActivity, MyVpnService::class.java).also {
            it.putExtra("IPV4",V4S)
            it.putExtra("IPV4L",V4L)
            it.action = MyVpnService.ACTION_CONNECT })
    }

    private fun startVpn() {
        //Tunnel.Start(UUID.randomUUID().toString(),save_config())
        myStart=true
        runBlocking {
            launch {
                var V4Addr:String="";
                while(myStart) {
                    //V4Addr=Tunnel.getVpnAddress();
                    if(!V4Addr.equals("0.0.0.0"))
                        break;
                    delay(1000)
                }
                if (myStart)
                    startVpn(V4Addr,24)
            }
        }
    }

    private fun stopVpn() {
        myStart=false
        //Tunnel.Stop()
        startService(Intent(this@MainActivity, MyVpnService::class.java).also { it.action = MyVpnService.ACTION_DISCONNECT })
    }

    class VpnContent : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == RESULT_OK
        }
    }

    private fun setupUI(isStart: Boolean? = null) {
        if (isStart == true) {
            //findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.stop_vpn)
        } else if (isStart == false) {
            //findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.start_vpn)
        } else {
            if (isMyVpnServiceRunning) {
                //findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.stop_vpn)
            } else {
                //findViewById<Button>(R.id.btnStartVPN).text = getString(R.string.start_vpn)
            }
        }
    }
    private fun load_config(){
        val filename = "tunnel.cfg"
        val file = File(this.filesDir, filename)
        if (file.canRead()) {
            val config = file.readText()
            if (config.isEmpty() == false) editText.setText(config)
        }
    }

    private fun save_config():String{
        val filename = "tunnel.cfg"
        val file = File(this.filesDir, filename)
        file.writeText(editText.text.toString())
        return file.absolutePath
    }
}
