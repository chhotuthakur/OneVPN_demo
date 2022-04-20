package com.one.hotspot.vpn.free.master.util.vpn.protocols

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.RemoteException
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.one.hotspot.vpn.free.master.manager.CacheManager
import com.one.hotspot.vpn.free.master.manager.VpnManager
import com.one.hotspot.vpn.free.master.util.Logger
import com.one.hotspot.vpn.free.master.util.vpn.server.OpenVpnServer
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNThread
import java.io.IOException
import java.util.*

class OpenVpn(val activity: Activity, val server: OpenVpnServer, val stateChangeListener: VpnManager.OnVpnStateChange?) {

    companion object {
        private const val TAG = "OpenVpnModule"
        private var retryCount = 0;
        private const val ConnectionStateIntentExtra = "state"
        private const val ConnectionStateIntentFilterAction = "connectionState"
    }

    private var vpnThread: OpenVPNThread = OpenVPNThread()


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val currentVpnState: String? = intent.getStringExtra(ConnectionStateIntentExtra)
                val detailStatus: String? = intent.getStringExtra("detailstatus")
                if (currentVpnState != null) {
                    if (currentVpnState.toLowerCase(Locale.getDefault()) == "connected") {
                        stateChangeListener?.onStateChange(VpnManager.VpnState.CONNECTED)
                    } else if (currentVpnState.toLowerCase(Locale.ROOT) == "exiting" || currentVpnState.toLowerCase(Locale.ROOT) == "noprocess") {
                        stateChangeListener?.onStateChange(VpnManager.VpnState.DISCONNECTING)
                    } else if (currentVpnState.toLowerCase(Locale.ROOT) == "disconnected") {
                        stateChangeListener?.onStateChange(VpnManager.VpnState.DISCONNECTED)
                    } else if (currentVpnState.toLowerCase(Locale.ROOT) == "connectretry") {
                        if (retryCount == 3) {
                            disconnect()
                            retryCount = 0;
                        } else {
                            retryCount++;
                            stateChangeListener?.onStateChange(VpnManager.VpnState.CONNECTING)
                        }
                    } else {
                        stateChangeListener?.onStateChange(VpnManager.VpnState.CONNECTING)
                    }
                }
            } catch (exception: Exception) {
                Logger.e(TAG, "Error to get Vpn Connection Status", exception)
            }
        }
    }

    init {
        val connectionIntentFilter = IntentFilter(ConnectionStateIntentFilterAction)
        LocalBroadcastManager.getInstance(CacheManager.getContext()?.applicationContext!!).registerReceiver(broadcastReceiver, connectionIntentFilter)
    }

    fun connect() {
        try {

            val serverContent = server.ovpnFile?.replace("<br>", "\n")

            OpenVpnApi.startVpn(CacheManager.getContext(), serverContent, server.country, server.username, server.password)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            vpnThread.stopProcess()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}