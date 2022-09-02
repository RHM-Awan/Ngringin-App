package com.rahmawan.ngringinapk.activity.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.user.DetailAgendaUserActivity
import com.rahmawan.ngringinapk.activity.user.MainUserActivity
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.firebase.agenda.GetNewAgenda
import com.rahmawan.ngringinapk.model.Agenda

class AgendaNotificationService : Service() {
    val getNewAgenda = GetNewAgenda
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getNewAgenda.getNewAgenda()
        getNewAgenda.setOnNewAgendasListener(object :GetNewAgenda.GetNewAgendaListener{  // set on buat agenda
            override fun onSuccess(result: Agenda) {
                showNotification("AGENDA BARU", "ADA AGENDA BARU ${result.title}", result.idAgenda)
            }
            override fun onCancelled(message: String) {}
        })
        return START_STICKY
    }

    fun showNotification(title: String?, message: String?, id:String?) {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24) // notif icon
                .setContentTitle(title) // title untuk notif
                .setContentText(message) // message untuk notif
                .setAutoCancel(true) // clear notif sebelum klik
        val intent = Intent(applicationContext, DetailAgendaUserActivity::class.java).putExtra("id", id)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }
}