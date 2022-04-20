
package com.one.hotspot.vpn.free.master.firebase

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.one.hotspot.vpn.free.master.manager.ServerManager
import com.one.hotspot.vpn.free.master.util.vpn.server.Ikev2Server
import com.one.hotspot.vpn.free.master.util.vpn.server.OpenVpnServer
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.util.Constants
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.collections.ArrayList


class DatabaseManager {

    companion object {
        var serversList = ArrayList<Server>()
        var certificateList = ArrayList<String>()
        private const val TAG = "DatabaseManager"
    }


    fun configFromUrl(name: String, context: Context): String{



        val url = if(name.startsWith("http"))
            name;
        else
            "http://snapvpn.lopstock.com/rapi/$name"

        val text: String
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connect()
            text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
        } finally {
            connection.disconnect()
        }

        return text


    }



    fun fetchServers(context: Context) {

        serversList.clear()

        val queue = Volley.newRequestQueue(context)
        val url = "http://snapvpn.lopstock.com/api.php?action=get_all_servers"

        val future = RequestFuture.newFuture<JSONObject>()
        val request = JsonObjectRequest(url, JSONObject(), future, future)
        queue.add(request)

        var response: JSONObject? = null

        try {
            response = future.get()

        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }


        if (response != null) {
            serversList=extractServers(response)
            ServerManager.setServers()
            Log.e("serverlistSize", serversList.size.toString());
        }else{
            serversList= extractServers(Constants.jsonObj)
            ServerManager.setServers()
        }





    }



  fun extractServers(dataSnapshot: JSONObject): ArrayList<Server> {
        val serverList: ArrayList<Server> = ArrayList()

        try {
            val responseJson = dataSnapshot
            Log.e("responseJson",responseJson.toString())
            val serverDetails = responseJson.getJSONArray("Servers")
            Log.e("serverDetails",serverDetails.toString())
            for(k in 0 until  serverDetails.length()){
                val json_obj =serverDetails.getJSONObject(k)

                val serverNameArray = json_obj.getString("serverName")
                val protocolArray = json_obj.getString("protocol")
                val usernameArray = json_obj.getString("username")
                val passwordArray = json_obj.getString("password")
                val configFileArray = json_obj.getString("configFile")
                val ipAddressArray = json_obj.getString("ipAddress")
                val gatewayArray = json_obj.getString("gateway")
                val latitudeArray = json_obj.getString("latitude")
                val longitudeArray = json_obj.getString("longitude")
                val isPremiumArray = json_obj.getString("type")
                val countriesArray = json_obj.getString("countries")

                val countryCodeArray = json_obj.getString("countryCode").toLowerCase(Locale.getDefault())

                val stateArray = json_obj.getString("state")





                var server: Server
                when (protocolArray) {
                    "IKEV2" -> {

                        certificateList.add(configFileArray)

                        server = Ikev2Server(k, serverNameArray, protocolArray, countriesArray, stateArray, usernameArray, passwordArray, ipAddressArray, gatewayArray, latitudeArray, longitudeArray, isPremiumArray, getFlagResourceId(countryCodeArray))
                    }
                    "OVPN-TCP",
                    "OVPN-UDP" -> {
                        server = OpenVpnServer(configFileArray, k, serverNameArray, protocolArray, countryCodeArray, stateArray, usernameArray, passwordArray, ipAddressArray, gatewayArray, latitudeArray, longitudeArray, isPremiumArray, getFlagResourceId(countryCodeArray))
                    }
                    else -> {
                        server = OpenVpnServer("", k, "", "", "", "", "", "", "", "", "", "", "", R.drawable.ic_globe)
                    }
                }
                serverList.add(server)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }


        return serverList
    }

    private fun getFlagResourceId(countryCode: String?): Int {
        if (countryCode != null) {
            return when (countryCode) {
                "united states" -> R.drawable.america
                "united arab" -> R.drawable.kuwait
                "india" -> R.drawable.india
                "japan" -> R.drawable.japan
                "switzerland" -> R.drawable.switzerland
                "germany" -> R.drawable.germany
                "south korea" -> R.drawable.southkorea
                "singapore" -> R.drawable.singapore
                "canada" -> R.drawable.canada
                "oman" -> R.drawable.oman
                "norway" -> R.drawable.norway
                "pakistan" -> R.drawable.pakistan
                "colombia" -> R.drawable.colombia
                "afghanistan" -> R.drawable.afghanistan
                "albania" -> R.drawable.albania
                "argentina" -> R.drawable.argentina
                "austria" -> R.drawable.austria
                "australia" -> R.drawable.australia
                "bangladesh" -> R.drawable.bangladesh
                "brazil" -> R.drawable.brazil
                "bolivia" -> R.drawable.bolivia
                "bulgaria" -> R.drawable.bulgaria
                "italy" -> R.drawable.italy
                "saudi arabia" -> R.drawable.ksa
                "united kingdom" -> R.drawable.uk

                else -> R.drawable.ic_globe
            }
        } else {
            return R.drawable.ic_globe
        }
    }

}