package com.example.testsample.utils

object Constance {
    var isMyVpnServiceRunning = false
    var vpnPermission = false
    var activeProfile = -1
    var policyList = arrayListOf<String>()
    var policy = ""
    const val PREFS_NAME = "MyPrefs"
    const val ALLOW_KEY = "allow"
    const val DISALLOW_KEY = "disallow"
    const val DISABLE_KEY = "disable"
}