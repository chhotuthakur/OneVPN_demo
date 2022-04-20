package com.one.hotspot.vpn.free.master.util.push

import com.one.hotspot.vpn.free.master.util.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class VpnFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val TAG = "VpnFirebase"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        Logger.d(TAG, "Refreshed token: $token")

    }
}