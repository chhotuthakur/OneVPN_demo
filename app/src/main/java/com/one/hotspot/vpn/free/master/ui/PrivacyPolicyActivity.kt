
package com.one.hotspot.vpn.free.master.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.databinding.ActivityWebViewBinding

import com.pixplicity.easyprefs.library.Prefs

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { finishAffinity() }

        binding.acceptButton.setOnClickListener {
            Prefs.putBoolean(Constants.PREF_KEY_IS_FIRST_TIME_USER, false)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}