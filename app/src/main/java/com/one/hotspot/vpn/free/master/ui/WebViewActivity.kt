
package com.one.hotspot.vpn.free.master.ui

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.one.hotspot.vpn.free.master.databinding.ActivityWebView2Binding


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebView2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebView2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }
        binding.webView.loadUrl("file:///android_asset/privacy.html")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                binding.webView.loadUrl(url!!)
                return true
            }
        }
    }
}