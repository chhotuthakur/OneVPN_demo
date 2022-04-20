package com.one.hotspot.vpn.free.master.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.one.hotspot.vpn.free.master.fragment.AppsFragment
import com.one.hotspot.vpn.free.master.util.AppUtils
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.navigateToActivity
import com.one.hotspot.vpn.free.master.util.showToast
import com.one.hotspot.vpn.free.master.BuildConfig
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private val appsFragment = AppsFragment(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crossIcon.setOnClickListener(this)

        binding.shareButton.setOnClickListener(this)
        binding.proxyApps.setOnClickListener(this)
        binding.contactUs.setOnClickListener(this)
        binding.privacyPolicy.setOnClickListener(this)
        binding.aboutUs.setOnClickListener(this)
        binding.appSelection.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.crossIcon.id -> onBackPressed()

            binding.privacyPolicy.id -> {
                navigateToActivity(WebViewActivity::class.java)
            }
            binding.appSelection.id -> {
                binding.fragLay.visibility=View.VISIBLE
               supportFragmentManager.beginTransaction().add(R.id.fragLay,appsFragment).commitAllowingStateLoss()
            }
            binding.contactUs.id -> {
                try {
                    val mailintent = Intent(Intent.ACTION_SEND)
                    mailintent.type = "text/plain"
                    mailintent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.CONTACT_US_EMAIL))
                    mailintent.putExtra(Intent.EXTRA_SUBJECT, "Share your feedback")
                    startActivity(Intent.createChooser(mailintent, "Sending mail..."))
                } catch (ignore: Exception) {
                    Toast.makeText(this, "Unable to found application to open", Toast.LENGTH_SHORT).show()
                }
            }
            binding.aboutUs.id -> {
                val appVersion = BuildConfig.VERSION_NAME
                Toast.makeText(this, "App Version : $appVersion", Toast.LENGTH_SHORT).show()
            }
            binding.shareButton.id -> AppUtils.shareApp(this)
            else -> showToast("Button clicked")
        }
    }

    override fun onBackPressed() {

        if(supportFragmentManager.fragments.contains(appsFragment)) {
            binding.fragLay.visibility = View.GONE
            supportFragmentManager.beginTransaction().remove(appsFragment).commitAllowingStateLoss()
            Log.e("onback","contains")
        }else
            super.onBackPressed()

    }
}