
package com.one.hotspot.vpn.free.master.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.one.hotspot.vpn.free.master.manager.VpnManager
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.BuildConfig
import com.one.hotspot.vpn.free.master.R
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.one.hotspot.vpn.free.master.databinding.ActivityConnectSuccessBinding
import com.pixplicity.easyprefs.library.Prefs
import org.strongswan.android.logic.MainApplication

class ConnectSuccessActivity : AppCompatActivity(), View.OnClickListener {



    private lateinit var timer: CountDownTimer
    private lateinit var binding: ActivityConnectSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConnectSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ipAddress = intent.getStringExtra(Constants.INTENT_EXTRA_KEY_IP)
        val serverCountry = intent.getStringExtra(Constants.INTENT_EXTRA_KEY_SERVER_COUNTRY)
        val flagResourceId = intent.getIntExtra(Constants.INTENT_EXTRA_KEY_SERVER_FLAG_ID, 0)
        val vpnConnectionState = intent.getIntExtra(Constants.INTENT_EXTRA_KEY_VPN_CONNECTION_STATE, -1)

        binding.serverIpAddress.text = ipAddress
        binding.serverName.text = serverCountry
        try {
            binding.serverFlagIcon.setImageDrawable(ContextCompat.getDrawable(this, flagResourceId))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.backIcon.setOnClickListener { onBackPressed() }

        binding.rateLayout.rateStar1.setOnClickListener(this)
        binding.rateLayout.rateStar2.setOnClickListener(this)
        binding.rateLayout.rateStar3.setOnClickListener(this)
        binding.rateLayout.rateStar4.setOnClickListener(this)
        binding.rateLayout.rateStar5.setOnClickListener(this)

        val vpnConnectedTimestamp = Prefs.getLong(Constants.PREF_KEY_VPN_CONNECTED_TIMESTAMP, System.currentTimeMillis())
        var vpnConnectionDuration = System.currentTimeMillis() - vpnConnectedTimestamp
        val second: Long = vpnConnectionDuration / 1000 % 60
        val minute: Long = vpnConnectionDuration / (1000 * 60) % 60
        val hour: Long = vpnConnectionDuration / (1000 * 60 * 60) % 24

        val workingHoursString = String.format("%02dh %02dm %02ds", hour, minute, second)
        binding.durationText.text = workingHoursString

        timer = object : CountDownTimer(24 * 60 * 60 * 1000, 1000) {
            override fun onTick(p0: Long) {
                if (vpnConnectionState == VpnManager.VpnState.CONNECTED) {
                    vpnConnectionDuration += 1000
                    val secondInner: Long = vpnConnectionDuration / 1000 % 60
                    val minuteInner: Long = vpnConnectionDuration / (1000 * 60) % 60
                    val hourInner: Long = vpnConnectionDuration / (1000 * 60 * 60) % 24

                    val workingHoursStringInner = String.format("%02dh %02dm %02ds", hourInner, minuteInner, secondInner)
                    binding.durationText.text = workingHoursStringInner
                }
            }

            override fun onFinish() {

            }

        }.start()
        val adLoader = AdLoader.Builder(this, MainApplication.NATIVE_ADMOB).forUnifiedNativeAd {
            val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.app_background))).build()
            val adView = binding.nativeAdView
            adView.setStyles(styles)
            adView.setNativeAd(it)
        }.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.rateLayout.rateStar1.id -> {
                binding.rateLayout.rateStar1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
            }
            binding.rateLayout.rateStar2.id -> {
                binding.rateLayout.rateStar1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
            }
            binding.rateLayout.rateStar3.id -> {
                binding.rateLayout.rateStar1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
                binding.rateLayout.rateStar5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
            }
            binding.rateLayout.rateStar4.id -> {
                binding.rateLayout.rateStar1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_star_yellow))
            }
            binding.rateLayout.rateStar5.id -> {
                binding.rateLayout.rateStar1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
                binding.rateLayout.rateStar5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rate_ic_yellow_selected))
            }
        }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
    }
}