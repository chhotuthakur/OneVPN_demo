package com.one.hotspot.vpn.free.master.util

import android.util.Log

object Logger {

    private const val APP_TAG = "SuperVpn: "
    private var isEnabled: Boolean = true

    fun d(tag: String, message: String) {
        if (isEnabled) {
            Log.d(APP_TAG + tag, message)
        }
    }

    fun d(tag: String, message: String, throwable: Throwable) {
        if (isEnabled) {
            Log.d(APP_TAG + tag, message, throwable)
        }
    }

    fun i(tag: String, message: String) {
        if (isEnabled) {
            Log.i(APP_TAG + tag, message)
        }
    }

    fun e(tag: String, message: String) {
        if (isEnabled) {
            Log.e(APP_TAG + tag, message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(APP_TAG + tag, message, throwable)
    }

}