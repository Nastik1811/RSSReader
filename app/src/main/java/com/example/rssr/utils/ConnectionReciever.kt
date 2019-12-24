package com.example.rssr.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast

@Suppress("DEPRECATION")
class ConnectionReceiver : BroadcastReceiver() {

    private var wasDisconnected: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected && wasDisconnected) {
            Toast.makeText(context, "Internet is connected", Toast.LENGTH_LONG).show()
        }
        else if(!isConnected) {
            Toast.makeText(context, "Disconnected network", Toast.LENGTH_LONG).show()
            wasDisconnected = true
        }
    }
}