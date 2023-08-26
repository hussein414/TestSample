package com.example.testsample.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.testsample.utils.Constance.isMyVpnServiceRunning
import com.example.testsample.vpnclient.vpn.tlsVPNService
import java.io.File
import java.util.UUID

class VpnCaller(private var context: Context) {

    fun getUUID(): String {
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId != null && androidId.isNotEmpty()) {
            println("ANDROID_ID: $androidId")
            return UUID.nameUUIDFromBytes(androidId.toByteArray()).toString()
        } else {
            return UUID.randomUUID().toString()
        }
    }

    var myStart: Boolean = false;
    fun startVpn(multiline: String) {
        context.startService(Intent(context, tlsVPNService::class.java).also {
            it.putExtra("CONFIG_NAME", saveConfig(multiline))
            it.putExtra("UUID", getUUID())
            it.putStringArrayListExtra("POLICY_LIST", Constance.policyList)
            it.action = tlsVPNService.ACTION_CONNECT
        })
    }

    fun stopVpn() {
        context.startService(Intent(context, tlsVPNService::class.java).also {
            it.action = tlsVPNService.ACTION_DISCONNECT
        })
    }


    private fun saveConfig(config: String): String {
        val filename = "tlsTunnel.cfg"
        val file = File(context.filesDir, filename)
        file.writeText(config)
        return file.absolutePath
    }

    private fun setupUI(isStart: Boolean? = null) {
        if (isStart == true) {

        } else if (isStart == false) {

        } else {
            if (isMyVpnServiceRunning) {

            } else {

            }
        }
    }

}