package com.one.hotspot.vpn.free.master.util.vpn.server

data class OpenVpnServer(val ovpnFile: String?, override var serverId: Int, override var serverName: String,
                         override var protocol: String, override var country: String, override var city: String,
                         override var username: String, override var password: String, override var ipAddress: String,
                         override var gateway: String, override var latitude: String, override var longitude: String,
                         override var isPremium: String, override var flag: Int) : Server() {


    override fun toString(): String {
        return "OpenVpnServer[FileName:$ovpnFile ServerId: $serverId ServerName: $serverName Protocol: $protocol Country: $country " +
                "City: $city Username: $username Password: $password IpAddress: $ipAddress Gateway: $gateway" +
                "Latitude: $latitude Longitude: $longitude IsPremium: $isPremium Latency: ${getLatency()}]"
    }
}
