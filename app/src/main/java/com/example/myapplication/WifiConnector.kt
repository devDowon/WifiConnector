package com.example.myapplication

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion

class WifiConnector(private val currentContext: Context) {
    fun connectToWifi(ssid: String, password: String) {
        val suggestion = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setWpa2Passphrase(password)
            .build()

        val suggestionsList = listOf(suggestion)
        val wifiManager = currentContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiManager.addNetworkSuggestions(suggestionsList)
    }
}