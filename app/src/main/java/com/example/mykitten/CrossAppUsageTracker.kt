package com.example.mykitten

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings

object CrossAppUsageTracker {

    private val doomscrollPackages = listOf(
        "com.instagram.android",
        "com.zhiliaoapp.musically", // TikTok
        "com.ss.android.ugc.trill", // TikTok global
        "com.google.android.youtube",
        "com.facebook.katana",
        "com.twitter.android",
        "com.reddit.frontpage",
        "com.snapchat.android"
    )

    fun hasUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun requestUsageStatsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isDoomscrollingActive(context: Context): Boolean {
        if (!hasUsageStatsPermission(context)) return false

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return false
        val currentTime = System.currentTimeMillis()
        // Query events from the last 15 seconds
        val stats = usageStatsManager.queryEvents(currentTime - 15000L, currentTime)

        var currentPackage = ""
        val event = UsageEvents.Event()
        while (stats.hasNextEvent()) {
            stats.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                currentPackage = event.packageName
            }
        }

        return doomscrollPackages.contains(currentPackage)
    }
}
