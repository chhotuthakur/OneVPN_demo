package com.one.hotspot.vpn.free.master.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.one.hotspot.vpn.free.master.R
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.one.hotspot.vpn.free.master.databinding.ActivityCustomAdBinding

class CustomAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomAdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomAdBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonCross.setOnClickListener {
            finish()
        }


        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110").forUnifiedNativeAd {
            val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.app_background))).build()
            val adView = binding.nativeAdView
            adView.setStyles(styles)
            adView.setNativeAd(it)
        }.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun onBackPressed() {

    }
}