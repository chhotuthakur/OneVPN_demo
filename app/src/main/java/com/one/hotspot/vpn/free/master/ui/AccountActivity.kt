package com.one.hotspot.vpn.free.master.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.one.hotspot.vpn.free.master.databinding.ActivityAccountBinding
import com.one.hotspot.vpn.free.master.util.showToast

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        binding.resotrePurchaseButton.setOnClickListener {
            showToast("${binding.resotrePurchaseButton.text} clicked.")
        }
    }
}