package com.one.hotspot.vpn.free.master.util.vpn.protocols

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.one.hotspot.vpn.free.master.manager.ActivityManager
import com.one.hotspot.vpn.free.master.manager.VpnManager
import com.one.hotspot.vpn.free.master.util.Logger
import com.one.hotspot.vpn.free.master.util.vpn.server.Ikev2Server
import org.strongswan.android.data.VpnProfile
import org.strongswan.android.data.VpnProfileDataSource
import org.strongswan.android.data.VpnType
import org.strongswan.android.data.VpnType.VpnTypeFeature
import org.strongswan.android.logic.VpnStateService
import org.strongswan.android.ui.VpnProfileControlActivity

private const val TAG = "Ikev2Module"

class Ikev2(val activity: Activity,val stateChangeListener: VpnManager.OnVpnStateChange?): ActivityManager.ActivityStateCallback {

    private var vpnProfile: VpnProfile? = null
    private var mService: VpnStateService? = null

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = (service as VpnStateService.LocalBinder).service
            mService?.setOnVpnChangeListener(stateChangeListener)
        }
    }

    init {
        activity.bindService(Intent(activity, VpnStateService::class.java), mServiceConnection, Service.BIND_AUTO_CREATE)
        ActivityManager.getInstance().currentState(this)
    }

    private  var server: Ikev2Server? = null
    fun setServer(  server: Ikev2Server ) {
        this.server = server
        val vpnProfileDataSource = VpnProfileDataSource(activity)
        vpnProfileDataSource.open()
        if (vpnProfile != null) {
            val profiles = vpnProfileDataSource.allVpnProfiles
            for (i in 0 until profiles.size) {
                val profile = profiles[i]
                if (server.ipAddress == profile.name) {
                    vpnProfile = profile
                    break
                }
            }
        }
        if (vpnProfile == null) {
            vpnProfile = VpnProfile()
            vpnProfile?.name = server.ipAddress
            vpnProfile?.username = server.username
            vpnProfile?.password = server.password
            vpnProfile?.vpnType = VpnType.IKEV2_EAP
            vpnProfile?.gateway = server.gateway
            vpnProfile?.flags = 0
            vpnProfile?.country = server.country
            vpnProfile?.selectedAppsHandling = VpnProfile.SelectedAppsHandling.SELECTED_APPS_ONLY


            vpnProfileDataSource.insertProfile(vpnProfile)
        } else {
            Logger.d(TAG, "onCreate: profile is not null : " + vpnProfile?.gateway)
        }
        vpnProfileDataSource.close()
    }


    fun connect() {
        VpnProfileControlActivity.registerOnVpnStateChange(stateChangeListener)
        startVpnProfile(vpnProfile?.uuid.toString())
    }

    fun disconnect() {
        if (mService != null) {
            if (mService!!.state == VpnStateService.State.CONNECTED ||
                    mService!!.state == VpnStateService.State.CONNECTING) {
                mService!!.disconnect()
            }
        }

    }

    private fun startVpnProfile(profileUUID: String) {
        val dataSource = VpnProfileDataSource(activity)
        dataSource.open()
        val profile = dataSource.getVpnProfile(profileUUID)
        dataSource.close()
        if (profile != null) {
            startVpnProfile(profile)
        }
    }

    private fun startVpnProfile(profileInfo: Bundle) {
        mService!!.connect(profileInfo, true)
    }

    private fun startVpnProfile(profile: VpnProfile) {
        val profileInfo = Bundle()
        profileInfo.putString(VpnProfileDataSource.KEY_UUID, profile.uuid.toString())
        profileInfo.putString(VpnProfileDataSource.KEY_USERNAME, profile.username)
        profileInfo.putString(VpnProfileDataSource.KEY_PASSWORD, profile.password)
        profileInfo.putBoolean(VpnProfileControlActivity.PROFILE_REQUIRES_PASSWORD, profile.vpnType.has(VpnTypeFeature.USER_PASS))
        profileInfo.putString(VpnProfileControlActivity.PROFILE_NAME, profile.name)
        profileInfo.putString(VpnProfileControlActivity.COUNTRY, profile.country)
        if (isConnected()) {
            stateChangeListener?.onStateChange(VpnManager.VpnState.CONNECTED)
            return
        }
        startVpnProfile(profileInfo)
    }

    private fun isConnected(): Boolean {
        if (mService == null) {
            return false
        }
        return if (mService!!.errorState != VpnStateService.ErrorState.NO_ERROR) {
            false
        } else mService!!.state == VpnStateService.State.CONNECTED || mService!!.state == VpnStateService.State.CONNECTING
    }

    override fun onCreate() {

    }

    override fun onDestroy() {
        if (mService != null) {
            activity.unbindService(mServiceConnection)
        }
    }

}