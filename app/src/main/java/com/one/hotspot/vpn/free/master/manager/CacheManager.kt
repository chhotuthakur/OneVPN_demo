package com.one.hotspot.vpn.free.master.manager

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object CacheManager {

    private var context: Context? = null

    fun getContext(): Context? {
        return context
    }

    fun setContext(context: Context?) {
        this.context = context
    }

}