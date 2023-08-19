package com.fanoos.vpnclient.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.annotation.RequiresApi
import com.example.testsample.ui.activites.MainActivity
import com.example.testsample.utils.isMyVpnServiceRunning


class MyVpnService : VpnService() {
    private var V4Address:String = "";
    private var V4PL:Int = 0;
    private lateinit var vpnInterface: ParcelFileDescriptor
    private val mConfigureIntent: PendingIntent by lazy {
        var activityFlag = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            activityFlag += PendingIntent.FLAG_MUTABLE
        }
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), activityFlag)
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "MyVpnService.Example"
        const val ACTION_CONNECT = "com.fanoos.vpnclient.CONNECT"
        const val ACTION_DISCONNECT = "com.fanoos.vpnclient.DISCONNECT"
    }

    override fun onCreate() {
        //our threads should be start here
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.action == ACTION_DISCONNECT) {
            disconnect()
            START_NOT_STICKY
        } else {
            V4Address=intent?.getStringExtra("IPV4").toString();
            V4PL= intent?.getIntExtra("IPV4L",0)!!;
            connect()
            START_STICKY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnect()
    }

    private fun connect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateForegroundNotification()
        }
        vpnInterface = createVPNInterface()
        isMyVpnServiceRunning = true
    }

    private fun disconnect() {
        vpnInterface.close()
        stopForeground(true)
        isMyVpnServiceRunning = false
        //our threads should be stop here
    }

    private fun createVPNInterface(): ParcelFileDescriptor {
        return Builder()
            .addAddress(V4Address, V4PL)
            .addDnsServer("4.2.2.4").addDnsServer("8.8.8.8")
            .addRoute("0.0.0.0", 0)
            .setBlocking(false)
            .addDisallowedApplication(getPackageName())
            .establish() ?: throw IllegalStateException("createVPNInterface illegal state")
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