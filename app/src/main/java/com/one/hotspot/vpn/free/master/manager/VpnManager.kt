package com.one.hotspot.vpn.free.master.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import com.one.hotspot.vpn.free.master.firebase.DatabaseManager
import com.one.hotspot.vpn.free.master.util.CertificateImporter
import com.one.hotspot.vpn.free.master.util.vpn.protocols.Ikev2
import com.one.hotspot.vpn.free.master.util.vpn.protocols.OpenVpn
import com.one.hotspot.vpn.free.master.util.vpn.server.Ikev2Server
import com.one.hotspot.vpn.free.master.util.vpn.server.OpenVpnServer
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import de.blinkt.openvpn.core.VpnStatus

class VpnManager(val activity: Activity, val stateChangeListener: OnVpnStateChange, certificates: ArrayList<String>) {

    private var openVpnModule: OpenVpn? = null
    private var ikev2VpnModule: Ikev2? = null
    private var onVpnStateChange: OnVpnStateChange? = null



    init {
        VpnStatus.initLogCache(activity.cacheDir)
        for (certFileName in certificates) {
            val isInstall = CertificateImporter.import(certFileName)
        }
        ikev2VpnModule = Ikev2(activity, stateChangeListener)
    }

    fun prepareVpn(): Intent? {
        return VpnService.prepare(activity)
    }

    fun connect(server: Server, context: Context) {
        if (server is OpenVpnServer) {
            Thread {
            val db = DatabaseManager()
            val ovpnConfig = db.configFromUrl(server.ovpnFile!!,context)

            val servernew= OpenVpnServer(ovpnConfig, server.serverId, server.serverName, server.protocol, server.country, server.city, server.username, server.password, server.ipAddress, server.gateway, server.latitude, server.longitude, server.isPremium, server.flag)

            openVpnModule = OpenVpn(activity, servernew, stateChangeListener)
            openVpnModule?.connect()

            }.start()
        } else if (server is Ikev2Server) {
            ikev2VpnModule?.setServer(server)
            ikev2VpnModule?.connect()
        }
    }

    fun disconnect() {
        openVpnModule?.disconnect()
        ikev2VpnModule?.disconnect()
    }

    fun setOnVpnStateChange(onVpnStateChange: OnVpnStateChange) {
        this.onVpnStateChange = onVpnStateChange
    }

    interface OnVpnStateChange {
        fun onStateChange(state: Int)
    }

    object VpnState {
        const val ERROR = -1
        const val DISCONNECTED = 0
        const val CONNECTED = 1
        const val CONNECTING = 2
        const val DISCONNECTING = 3
        const val AUTHENTICATING = 4
    }
}