package com.one.hotspot.vpn.free.master.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(stringId: Int) {
    Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
}

fun Context.navigateToActivity(javaClass: Class<*>) {
    startActivity(Intent(this, javaClass))
}

