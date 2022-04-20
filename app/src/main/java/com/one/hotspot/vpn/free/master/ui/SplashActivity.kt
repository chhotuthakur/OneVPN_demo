package com.one.hotspot.vpn.free.master.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.LoadServers
import com.one.hotspot.vpn.free.master.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val queue = Volley.newRequestQueue(this)

        val url = "http://snapvpn.lopstock.com/api.php?action=get_all_servers"
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    LoadServers.api_error = "1"
                    Log.e("res",""+response)
                    val asyc= LoadServers(this@SplashActivity,this@SplashActivity)
                    asyc.execute(response)
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                LoadServers.api_error = "2"
                runOnUiThread(Runnable { Toast.makeText(this@SplashActivity,"Something went wrong!",Toast.LENGTH_SHORT).show() })
                val asyc= LoadServers(this@SplashActivity,this@SplashActivity)
                asyc.execute(Constants.backup_json)


            }
        })


        queue.add(stringRequest)

    }
}