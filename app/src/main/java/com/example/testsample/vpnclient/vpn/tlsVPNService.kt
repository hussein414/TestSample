package com.example.testsample.vpnclient.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.vpnclient.tlsTunnel


class tlsVPNService : VpnService() {
    var uuid:String=""
    private val MONITOR_INTERVAL: Long = 1000 //in MS
    private var V4Address:String = "0.0.0.0"
    private var V4PL:Int = 0;
    private var connected = false
    private lateinit var vpnInterface: ParcelFileDescriptor

    private fun showToast(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(this@tlsVPNService, message, Toast.LENGTH_SHORT).show()
        }
    }

    private val mConfigureIntent: PendingIntent by lazy {
        var activityFlag = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            activityFlag += PendingIntent.FLAG_MUTABLE
        }
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), activityFlag)
    }

    private val monitorHandler = Handler(Looper.getMainLooper())
    private val monitorRunnable: Runnable = object : Runnable {
        override fun run() {
            synchronized(isMyVpnServiceRunning) {
                if (isMyVpnServiceRunning) {
                    var newV4Address = tlsTunnel.getVpnAddress()
                    if (newV4Address.equals(V4Address)==false) {
                        V4Address=newV4Address
                        showToast("address "+V4Address+" assigned")
                        if (connected)
                            disconnect()
                        if (newV4Address.equals("0.0.0.0")==false)
                            connect()
                    }
                }
            }
            monitorHandler.postDelayed(this, MONITOR_INTERVAL)
        }
    }
    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "MyVpnService.Example"
        const val ACTION_CONNECT = "com.fanoos.vpnclient.CONNECT"
        const val ACTION_DISCONNECT = "com.fanoos.vpnclient.DISCONNECT"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.action == ACTION_DISCONNECT) {
            showToast("vpn stop!!!")
            vpnStop()
            START_NOT_STICKY
        } else {
            showToast("vpn start!!!")
            uuid=intent?.getStringExtra("UUID").toString()
            vpnStart(intent?.getStringExtra("CONFIG_NAME").toString())
            START_STICKY
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroy() {
        super.onDestroy()
        vpnStop()
    }
    private fun vpnStop() {
        synchronized(isMyVpnServiceRunning) {
            if (isMyVpnServiceRunning) {
                isMyVpnServiceRunning = false
                monitorHandler.removeCallbacks(monitorRunnable)
                tlsTunnel.tunnelStop()
                if (connected)
                    disconnect()
                stopForeground(true)
            }
        }
    }
    private fun vpnStart(configName:String){
        synchronized(isMyVpnServiceRunning){
            if (isMyVpnServiceRunning ==false){
                V4Address="0.0.0.0"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    updateForegroundNotification()
                }
                isMyVpnServiceRunning = true
                tlsTunnel.tunnelStart(uuid,configName)
                monitorHandler.postDelayed(monitorRunnable, MONITOR_INTERVAL)
            }
        }
    }
    private fun connect() {
        vpnInterface = createVPNInterface()
        tlsTunnel.setTunnelFd(vpnInterface.fd)
        connected=true
        showToast("tunnel start")
    }

    private fun disconnect() {
        tlsTunnel.setTunnelFd(-1)
        vpnInterface.close()
        connected=false
        showToast("tunnel stop")
    }

    private fun createVPNInterface(): ParcelFileDescriptor {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Builder()
                .addAddress(V4Address, V4PL)
                .addDnsServer("4.2.2.4").addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)
                .setBlocking(false)
                .addDisallowedApplication(getPackageName())
                .establish() ?: throw IllegalStateException("createVPNInterface illegal state")
        } else {
            Builder()
                .addAddress(V4Address, V4PL)
                .addDnsServer("4.2.2.4").addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)
                .setBlocking(false)
                .addDisallowedApplication(getPackageName())
                .establish() ?: throw IllegalStateException("createVPNInterface illegal state")       }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateForegroundNotification() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentText("VPN Running")
            .setContentIntent(mConfigureIntent)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }
}