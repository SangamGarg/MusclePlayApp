package com.sangam.muscleplay.appUtils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return run {
        val activateNetwork = connectivityManager.activeNetwork ?: return false
        val cap = connectivityManager.getNetworkCapabilities(activateNetwork) ?: return false
        when {
            cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            cap.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
            else -> false
        }
    }
}