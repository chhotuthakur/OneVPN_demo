
package com.one.hotspot.vpn.free.master.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.one.hotspot.vpn.free.master.BuildConfig


object AppUtils {

    fun shareApp(activity : Activity) {
        val appLinkString = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, appLinkString)
        sendIntent.type = "text/plain"
        activity.startActivity(sendIntent)
    }
}