package com.rahmawan.ngringinapk.activity.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.user.DetailAgendaUserActivity

class AgendaAlarmReceiver:BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val b = p1?.extras
        showNotification(p0!!, "AGENDA","Agenda ${b?.getString("title")} 1 jam lagi", b?.getString("id"))
    }

    fun showNotification(ctx:Context,title: String?, message: String?, id:String?) {
        val mNotificationManager = NotificationManagerCompat.from(ctx)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(ctx, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message) // message for notification
                .setAutoCancel(true) // clear notification after click
        val intent = Intent(ctx, DetailAgendaUserActivity::class.java).putExtra("id", id)
        val pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(1, mBuilder.build())
    }
}