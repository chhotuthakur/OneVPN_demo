package com.one.hotspot.vpn.free.master.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.one.hotspot.vpn.free.master.DataManager
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.adapter.PremiumFeaturesAdapter
import com.one.hotspot.vpn.free.master.databinding.ActivityPremiumBinding
import com.one.hotspot.vpn.free.master.model.PremiumFeature
import com.one.hotspot.vpn.free.master.util.SharedPreferencesManager


class PremiumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPremiumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PremiumFeaturesAdapter(this@PremiumActivity, featuresList())
        binding.premiumFeatureViewPager.adapter = adapter
        binding.premiumFeatureViewPager.currentItem = 0
        binding.dotsIndicator.setViewPager(binding.premiumFeatureViewPager)

        binding.closeIcon.setOnClickListener { onBackPressed() }
        binding.monthSubscriptionLayout.setOnClickListener { monthlySubSelectUI() }
        binding.yearlySubscriptionLayout.setOnClickListener { yearlySubSelectUI() }


    }

    private fun featuresList(): List<PremiumFeature> {
        val list = ArrayList<PremiumFeature>()
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_fast), resources.getString(R.string.premium_feature_fast_desc), R.drawable.iap_ic_fast))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_secure), resources.getString(R.string.premium_feature_secure_desc), R.drawable.iap_ic_secure))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_unlimited), resources.getString(R.string.premium_feature_unlimited_desc), R.drawable.iap_ic_unlimited))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_no_logs), resources.getString(R.string.premium_feature_no_logs_desc), R.drawable.iap_ic_no_logs))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_anonymous), resources.getString(R.string.premium_feature_anonymous_desc), R.drawable.iap_ic_anonymous))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_vip_servers), resources.getString(R.string.premium_feature_vip_servers_desc), R.drawable.iap_ic_worldwide))
        list.add(PremiumFeature(resources.getString(R.string.premium_feature_remove_ads), resources.getString(R.string.premium_feature_remove_ads_desc), R.drawable.iap_ic_no_ads))

        return list
    }

    private fun monthlySubSelectUI() {
        binding.monthSubNum.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.monthSubMonth.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.monthSubCost.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.monthSubText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        binding.monthSubText.background = ContextCompat.getDrawable(this, R.drawable.blue_background_bottom_left_corner)

        binding.yearSubNum.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.yearSubMonth.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.yearSubCost.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.yearSubText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.yearSubSaving.setBackgroundColor(ContextCompat.getColor(this, R.color.pale_blue_light))
        binding.yearSubTextLayout.background = ContextCompat.getDrawable(this, R.drawable.white_background_bottom_corner)
        DataManager.packege_monthly = true
        DataManager.packege_yearly = false
    }

    private fun yearlySubSelectUI() {
        binding.monthSubNum.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.monthSubMonth.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.monthSubCost.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.monthSubText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.monthSubText.background = ContextCompat.getDrawable(this, R.drawable.white_background_bottom_corner)

        binding.yearSubNum.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.yearSubMonth.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.yearSubCost.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        binding.yearSubText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        binding.yearSubSaving.background = ContextCompat.getDrawable(this, R.drawable.blue_background)
        binding.yearSubTextLayout.background = ContextCompat.getDrawable(this, R.drawable.blue_background_bottom_right_corner)
        DataManager.packege_monthly = false
        DataManager.packege_yearly = true
    }


}