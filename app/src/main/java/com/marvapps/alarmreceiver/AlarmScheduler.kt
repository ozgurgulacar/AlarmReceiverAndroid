package com.marvapps.alarmreceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class AlarmScheduler(val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(time: LocalDateTime, hashcode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)


        val receiver = ComponentName(context, AlarmReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )




        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(context, hashcode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE),

        )



    }


    fun cancel(hashcode: Int){
        val intent = Intent(context, AlarmReceiver::class.java)

        alarmManager.cancel(
            PendingIntent.getBroadcast(context, hashcode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )
    }

}