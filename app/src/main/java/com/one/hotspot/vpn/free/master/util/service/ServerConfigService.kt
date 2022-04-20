package com.one.hotspot.vpn.free.master.util.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.one.hotspot.vpn.free.master.firebase.DatabaseManager
import com.one.hotspot.vpn.free.master.ui.HomeActivity
import com.one.hotspot.vpn.free.master.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ServerConfigService: Service() {

    companion object {
        const val TAG = "ServerConfigService"
        var listener : ServerConfigListener? = null
        fun startService(context: Context) {
            val startIntent = Intent(context, ServerConfigService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
            listener?.serverConfigCompleted(false)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ServerConfigService::class.java)
            context.stopService(stopIntent)
            Log.e("service stoped","true")
            listener?.serverConfigCompleted(true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.async {
            val serverDatabase = DatabaseManager()
            serverDatabase.fetchServers(applicationContext)
        }

        createNotificationChannel()
        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
                this,
                0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, "${this.getString(R.string.app_name)} Channel")
                .setContentTitle("${this.getString(R.string.notify_update_config_message)}")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel("${this.getString(R.string.app_name)} Channel",
                    "${this.getString(R.string.notify_update_config_message)} Channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    interface ServerConfigListener {
        fun serverConfigCompleted(success: Boolean)
    }
}
