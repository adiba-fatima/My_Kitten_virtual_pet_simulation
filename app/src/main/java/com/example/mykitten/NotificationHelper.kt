package com.example.mykitten

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    private const val CHANNEL_ID = "my_kitten_channel"
    private const val HUNGER_NOTIFICATION_ID = 1001
    private const val REST_NOTIFICATION_ID = 1002

    private var lastHungerAlertTime = 0L
    private var lastRestAlertTime = 0L
    private const val ALERT_COOLDOWN_MS = 1800000L // 30 minutes cooldown between same alerts

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Kitten Reminders"
            val descriptionText = "Notifications when your cat is hungry or tired"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun checkAndSendNotifications(context: Context, hunger: Double, rest: Double, petName: String) {
        val currentTime = System.currentTimeMillis()

        // Check Hunger alert (< 25%)
        if (hunger <= 25.0 && currentTime - lastHungerAlertTime > ALERT_COOLDOWN_MS) {
            sendNotification(
                context,
                HUNGER_NOTIFICATION_ID,
                "Feed your cat! 🐟",
                "$petName is getting very hungry! Open the app to feed your companion."
            )
            lastHungerAlertTime = currentTime
        }

        // Check Rest/Tired alert (< 25%)
        if (rest <= 25.0 && currentTime - lastRestAlertTime > ALERT_COOLDOWN_MS) {
            sendNotification(
                context,
                REST_NOTIFICATION_ID,
                "Time for a nap! 💤",
                "$petName is tired and needs to go to bed. Put your cat to sleep!"
            )
            lastRestAlertTime = currentTime
        }
    }

    private fun sendNotification(context: Context, notificationId: Int, title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.cat)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(notificationId, builder.build())
            } catch (e: SecurityException) {
                // Ignore if permission not granted
            }
        }
    }
}
