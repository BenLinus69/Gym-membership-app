package com.example.gymmembershipapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.core.app.NotificationCompat

class NetworkStateReceiver(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun registerNetworkCallback() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

                override fun onLost(network: Network) {
                    super.onLost(network)
                    showNoInternetNotification()
                }

        })

    }

    private fun showNoInternetNotification() {
        val notificationChannelId = "no_internet_channel"

            val channel = NotificationChannel(
                notificationChannelId,
                "No Internet",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)


        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("No Internet Connection")
            .setContentText("Your device is no longer connected to the internet.")
            .setSmallIcon(R.drawable.dumbbell)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
