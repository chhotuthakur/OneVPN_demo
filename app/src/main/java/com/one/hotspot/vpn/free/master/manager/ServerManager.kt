package com.one.hotspot.vpn.free.master.manager

import android.util.Log
import com.one.hotspot.vpn.free.master.firebase.DatabaseManager
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.Logger
import com.one.hotspot.vpn.free.master.util.service.ServerConfigService
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import com.one.hotspot.vpn.free.master.R
import com.pixplicity.easyprefs.library.Prefs
import java.io.IOException
import java.net.InetAddress
import kotlin.random.Random

object ServerManager {
    private const val TAG = "ServerManager"

    private fun init(): List<Server> {
        ArrayList<Server>()
       return DatabaseManager.serversList
    }

    fun getCertificates(): ArrayList<String> {
        return DatabaseManager.certificateList
    }

    private var servers = ArrayList<Server>()

    fun getServers(): ArrayList<Server> {
        return DatabaseManager.serversList
    }

    fun setServers() {
        val initServers = init()
        for (i in initServers.indices) {
            val server: Server = initServers[i]
            val latency = 1000L
            Logger.i(TAG, "${server.ipAddress}  = $latency")
            server.setLatency(latency)
            servers.add(server)
        }
        ServerConfigService.stopService(CacheManager.getContext()!!)
    }

    fun getServer(protocol: Protocol): Server {
        val serverId = Prefs.getInt(Constants.INTENT_EXTRA_KEY_SERVER, -1)
        var servers: ArrayList<Server> = ArrayList<Server>()
        Log.e("serverlistG",DatabaseManager.serversList.toString())
        Log.e("protocol",protocol.toString())
        when (protocol) {
            Protocol.IKEV2 -> {
                servers = getListIkev2Server(getServers())
            }
            Protocol.OVPNTCP -> {
                servers = getListTcpServer(getServers())
            }
            Protocol.OVPNUDP -> {
                servers = getListUdpServer(getServers())
            }
            Protocol.ALL -> {
                Log.e("else running", DatabaseManager.serversList.toString())
                servers = DatabaseManager.serversList
            }
        }
        return if (serverId == -1) {
            getRandomServer(servers)
        } else {
            getSelected(servers, serverId) ?: getRandomServer(servers)
        }
    }


    fun setLatency(){
        for (i in servers.indices) {
            val server: Server = servers[i]
            val latency = isReachable(server.ipAddress)
            Logger.i(TAG, "${server.ipAddress}  = $latency")
            server.setLatency(latency)
        }


    }


    private fun getListIkev2Server(servers: ArrayList<Server>): ArrayList<Server> {
        val protocolServers = ArrayList<Server>()
        for (server in servers) {
            if (server.protocol.contains("ikev", true)) {
                protocolServers.add(server)
            }
        }
        return protocolServers
    }

    private fun getListTcpServer(servers: ArrayList<Server>): ArrayList<Server> {
        val protocolServers = ArrayList<Server>()
        for (server in servers) {
            if (server.protocol.contains("tcp", true)) {
                protocolServers.add(server)
            }
        }
        return protocolServers
    }

    private fun getListUdpServer(servers: ArrayList<Server>): ArrayList<Server> {
        val protocolServers = ArrayList<Server>()
        for (server in servers) {
            if (server.protocol.contains("udp", true)) {
                protocolServers.add(server)
            }
        }
        return protocolServers
    }

    fun getTotalServers(): List<Server> {
        return getServers()
    }

    fun getCountryServerList(country: String): List<Server>? {
        val countryServerHashMap = totalCountryServerMap()
        return if (countryServerHashMap.containsKey(country)) {
            countryServerHashMap[country]
        } else {
            emptyList()
        }
    }

    fun totalCountryServerMap(): Map<String, List<Server>> = getTotalServers().groupBy { it.country }

    private fun getRandomServer(arrayList: ArrayList<Server>): Server {
        return if (arrayList.size > 1) arrayList[Random.nextInt(0, arrayList.size - 1)]
        else arrayList[0]
    }

    private fun getSelected(arrayList: ArrayList<Server>, serverId: Int): Server? {
        for (i in 0 until arrayList.size) {
            val server = arrayList[i]
            if (server.serverId == serverId) {
                return server
            }
        }
        return null
    }


    private fun isReachable(address: String): Long {
        return try {
            val startTime = System.currentTimeMillis()
            val inetAddress = InetAddress.getByName(address)
            val isReachable = inetAddress.isReachable(1000)
            if (isReachable) {
                val latency = (System.currentTimeMillis() - startTime)
                return if (latency > 1000) {
                    1000
                } else {
                    latency
                }
            }
            return 1000
        } catch (e: IOException) {
            return 1000
        }

    }

    enum class Protocol {
        IKEV2, OVPNTCP, OVPNUDP, ALL
    }

}