package com.one.hotspot.vpn.free.master.util.api.data

data class IpLookup(var status: String, var country: String, var countryCode: String, var region: String,
                    val regionName: String, var city: String, var zip: String, var lat: Double, var lon: Double, val timezone: String,
                    var isp: String, var org: String, var ast: String, var query: String)
