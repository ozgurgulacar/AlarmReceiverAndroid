package com.marvapps.alarmreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Telephony.Sms.Intents
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


class AlarmReceiver(): BroadcastReceiver() {


    var noti = null as NotificationManager?


    override fun onReceive(context: Context?, intent: Intent?) {
        val chanel = NotificationChannel(
            "channel_id",
            "channel_name",
            NotificationManager.IMPORTANCE_HIGH
        )




        noti  = getSystemService(context!!,NotificationManager::class.java)
        noti!!.createNotificationChannel(chanel)


        createNotification(context)

        println("Alarm Triggered")

    }


    fun createNotification(context: Context?){


        val notification=NotificationCompat.Builder(context!!,"channel_id")
            .setContentText("CONTENT......")
            .setSubText("SUBTEXT")
            .setContentTitle("Title")
            .setContentIntent(PendingIntent.getActivity(context,1,Intent(context,MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
        noti!!.notify(1,notification)

    }

}