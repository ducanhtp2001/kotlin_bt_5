package com.example.demoappworkreminder.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.demoappworkreminder.R

class WorkReminder(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {


    override fun doWork(): Result {

        val workContent = inputData.getString("workContent")
        val notificationId = inputData.getInt("notificationId", 0)

        if(workContent == null) Log.d("reminder_log", "work is null")

        var channel: NotificationChannel
        var notificationManager: NotificationManager
        val CHANNEL_ID = "myChannel"
        val notifi_ID = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)
            notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_icon)
                .setContentTitle(workContent)
                .setSound(null)
                .build()

            notificationManager.notify(notificationId, notification)

        }
        return Result.success()
    }
}