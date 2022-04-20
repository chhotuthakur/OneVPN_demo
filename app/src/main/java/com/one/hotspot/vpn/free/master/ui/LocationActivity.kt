
package com.one.hotspot.vpn.free.master.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.Logger
import com.one.hotspot.vpn.free.master.util.api.ApiInterface
import com.one.hotspot.vpn.free.master.util.api.data.IpLookup
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.ActivityLocationBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LocationActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "LocationActivity"
        @SuppressLint("StaticFieldLeak")
        private lateinit var activity: Activity
    }

    private lateinit var binding: ActivityLocationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this



        val isVpnConnected = intent.extras?.getBoolean("isVpnConnected")

        binding.backIcon.setOnClickListener { onBackPressed() }
        val ipAddress = intent.extras?.getString(Constants.INTENT_EXTRA_KEY_IP)
        GlobalScope.launch {
            val apiInterface = ApiInterface.createIp().getIpDetails(ipAddress)
            apiInterface.enqueue(object : Callback<IpLookup> {
                override fun onResponse(call: Call<IpLookup>?, response: Response<IpLookup>?) {
                    Logger.d(TAG, call.toString())
                    Logger.d(TAG, response?.message().toString())
                    if (response?.body() != null) {
                        Logger.e(TAG, "${response.body()}")
                        val ipInfo = response.body()
                        val webview = binding.mapWebView
                        webview.webViewClient = WebViewClient()
                        webview.settings.javaScriptEnabled = true
                        webview.settings.displayZoomControls = false
                        webview.settings.useWideViewPort = true

                        webview.loadUrl("https://www.google.com/maps/search/@?api=1&map_action=map&center=${ipInfo?.lat},${ipInfo?.lon}")

                        webview.setOnTouchListener { v, event -> true }

                        binding.mapLayout.setOnClickListener {
                            val uri: String = java.lang.String.format(Locale.ENGLISH, "https://maps.google.com/maps?q=loc:%f,%f", ipInfo?.lat, ipInfo?.lon)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        binding.latitudeTextValue.text = "${ipInfo?.lat}"
                        binding.longitudeTextValue.text = "${ipInfo?.lon}"
                        binding.cityTextValue.text = "${ipInfo?.city}"
                        binding.regionTextValue.text = "${ipInfo?.regionName}"
                        binding.countryTextValue.text = "${ipInfo?.country}"
                        binding.postalTextValue.text = "${ipInfo?.zip}"
                        ipInfo?.let { changeTitleCountryFlag(it.country) }
                    }
                }

                override fun onFailure(call: Call<IpLookup>?, t: Throwable?) {
                }
            })
        }

        if (isVpnConnected!!) {
            val flagId = intent.getIntExtra(Constants.INTENT_EXTRA_KEY_SERVER_FLAG_ID, R.drawable.ic_globe)
        }

    }

    private fun changeTitleCountryFlag(countryName: String) {
        when {
            countryName.equals("united states", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("canada", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("germany", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("japan", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }

            countryName.equals("singapore", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("south korea", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("united kingdom", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("switzerland", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("united arab emirates", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("dubai", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("qatar", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
            countryName.equals("abu dhabi", ignoreCase = true) -> {
                changeTitleRightDrawable()
            }
        }
    }

    private fun changeTitleRightDrawable() {

    }
}