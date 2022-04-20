package com.one.hotspot.vpn.free.master.util.vpn.server


abstract class Server() {
    abstract var serverId: Int
    abstract var serverName: String
    abstract var protocol: String
    abstract var country: String
    abstract var city: String
    abstract var username: String
    abstract var password: String
    abstract var ipAddress: String
    abstract var gateway: String
    abstract var latitude: String
    abstract var longitude: String
    abstract var isPremium: String
    abstract var flag: Int

    private var latency: Long? = 0

    fun getLatency(): Long? {
        return latency
    }

    fun setLatency(latency: Long) {
        this.latency = latency
    }
}

