package com.one.hotspot.vpn.free.master.ui


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils.UpdateListener
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.pixplicity.easyprefs.library.Prefs
import com.one.hotspot.vpn.free.master.DataManager
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.ActivityHomeBinding
import com.one.hotspot.vpn.free.master.firebase.DatabaseManager
import com.one.hotspot.vpn.free.master.manager.ActivityManager
import com.one.hotspot.vpn.free.master.manager.ServerManager
import com.one.hotspot.vpn.free.master.manager.VpnManager
import com.one.hotspot.vpn.free.master.util.*
import com.one.hotspot.vpn.free.master.util.service.ServerConfigService
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import org.jetbrains.anko.doAsync
import org.strongswan.android.logic.MainApplication
import java.util.*


class HomeActivity : AppCompatActivity(), View.OnClickListener, VpnManager.OnVpnStateChange, ChangeServerActivity.OnServerLocationChange {

    companion object {

        private const val TAG = "HomeActivity"
    }

    private var serverIp = ""
    private var serverLatency: Long? = 0L
    private var serverCountry = ""
    private var serverProtocol = ""
    private  var server: Server? = null
    var mAd: RewardedVideoAd? = null
    private  var context: Context? = null
    private  var binding: ActivityHomeBinding? = null
    private lateinit var interstitialAd: InterstitialAd
    private var selectedProtocol: ServerManager.Protocol = ServerManager.Protocol.ALL
    private var vpnConnected = false
    private lateinit var vpnManager: VpnManager
    private var onServerLocationChange: ChangeServerActivity.OnServerLocationChange? = null


    private var vpnState= VpnManager.VpnState.CONNECTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        SharedPreferencesManager.init(this)
        val prem_icon = findViewById<ImageView>(R.id.ic_premium)
        updateAvailable()
        mAd = MobileAds.getRewardedVideoAdInstance(this)
        context = this
        onServerLocationChange = this
        setupClickListeners()
        vpnManager = VpnManager(this, this, ServerManager.getCertificates())

        vpnManager.setOnVpnStateChange(this)


        if(isOnline() && !DatabaseManager.serversList.isEmpty()) {
            binding?.autoProtocol?.performClick()
        }else{

            binding?.changeServer?.visibility=View.GONE
            binding?.noserver?.visibility=View.VISIBLE
            binding?.vpnConnectionStatus?.text=getString(R.string.retry)


        }
        val mBannerAd = AdView(this)
        mBannerAd.setAdSize(AdSize.BANNER)
        mBannerAd.setAdUnitId(MainApplication.BANNER_ADMOB)
        val adContainer = findViewById<RelativeLayout>(R.id.banner)
        adContainer.addView(mBannerAd)
        val adRequest = AdRequest.Builder().build()
        mBannerAd.loadAd(adRequest)

        doAsync {
            ServerManager.setLatency()
        }


        serverIp = server?.ipAddress.toString()
        serverLatency = server?.getLatency()
        serverCountry = server?.country.toString()
        serverProtocol = server?.protocol.toString()

        binding?.connectButton?.tag = Prefs.getString(Constants.PREF_KEY_SELECTED_VPN_CONNECTION_TAG, "connect")

        if (!isVpnActive(this)) {
            binding?.connectButton?.tag = "connect"
        }

        checkPreviousVpnConnection()

        ActivityManager.getInstance().onCreate()

        val adLoader = AdLoader.Builder(this, MainApplication.NATIVE_ADMOB).forUnifiedNativeAd {
            val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(ContextCompat.getColor(applicationContext, R.color.app_background))).build()
            val adView = binding?.nativeAdView
            adView?.setStyles(styles)
            adView?.setNativeAd(it)
        }.build()
        adLoader.loadAd(AdRequest.Builder().build())





        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = MainApplication.INTERSTITIAL_ADMOB
        interstitialAd.loadAd(AdRequest.Builder().build())
        interstitialAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError?) {
                Logger.d(TAG, "Ad failed to load, error : $p0")
            }

            override fun onAdLoaded() {
                Logger.d(TAG, "Interstitial Ad loaded")

            }

            override fun onAdClosed() {
                interstitialAd.loadAd(AdRequest.Builder().build())
                openConnectSuccessActivity(vpnState)
            }
        }
        prem_icon.setOnClickListener(View.OnClickListener { view: View? ->
            val dialog = Premium_dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.show()
        })
        val prem_icon2 = findViewById<LinearLayout>(R.id.layout)
        prem_icon2.setOnClickListener(View.OnClickListener { view: View? ->
            val dialog = Premium_dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog.show()
        })
    }

    private fun setupClickListeners() {
        binding?.appBar?.icPremium?.visibility = View.VISIBLE

        binding?.appBar?.icHelp?.setOnClickListener(this)
        binding?.appBar?.icShare?.setOnClickListener(this)
        binding?.appBar?.menuIcon?.setOnClickListener(this)
        binding?.appBar?.icLocation?.setOnClickListener(this)
        binding?.appBar?.icShare?.setOnClickListener(this)
        binding?.appBar?.icHelp?.setOnClickListener(this)
        binding?.connectButton?.setOnClickListener(this)


        binding?.changeServerlyt2?.setOnClickListener(this)
        binding?.changeServer?.setOnClickListener(this)
        binding?.connectButton?.setOnClickListener(this)

        binding?.tcpProtocol?.setOnClickListener(this)
        binding?.udpProtocol?.setOnClickListener(this)
        binding?.autoProtocol?.setOnClickListener(this)
        binding?.ikev2Protocol?.setOnClickListener(this)
    }


    private fun checkPreviousVpnConnection() {
        if (binding?.connectButton?.tag == "connect") {
            disconnectedUI()
        } else {
            connectedUI()
            serverIp = Prefs.getString(Constants.PREF_KEY_SELECTED_SERVER_IP, "")
            serverCountry = Prefs.getString(Constants.PREF_KEY_SELECTED_SERVER_COUNTRY, "US")
            serverLatency = Prefs.getLong(Constants.PREF_KEY_SELECTED_SERVER_LATENCY, 0L)
            val serverFlag = Prefs.getInt(Constants.PREF_KEY_SELECTED_SERVER_COUNTRY_FLAG, R.drawable.america)
            serverProtocol = Prefs.getString(Constants.PREF_KEY_SELECTED_SERVER_PROTOCOL, ServerManager.Protocol.ALL.name)

            val protocol = when (serverProtocol) {
                ServerManager.Protocol.IKEV2.name -> ServerManager.Protocol.IKEV2
                ServerManager.Protocol.OVPNTCP.name -> ServerManager.Protocol.OVPNTCP
                ServerManager.Protocol.OVPNUDP.name -> ServerManager.Protocol.OVPNUDP
                else -> ServerManager.Protocol.ALL
            }
            activeServerProtocol(protocol)
            binding?.changeServer?.setText(serverCountry)
            binding?.ivFlag?.setImageResource(serverFlag)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            binding?.appBar?.icShare?.id -> AppUtils.shareApp(this@HomeActivity)
            binding?.appBar?.icHelp?.id -> navigateToActivity(HelpActivity::class.java)
            binding?.appBar?.menuIcon?.id -> navigateToActivity(SettingsActivity::class.java)
            binding?.changeServerlyt2?.id -> {

                ChangeServerActivity.instance(this, server!!, selectedProtocol.name, onServerLocationChange!!)
            }
            binding?.changeServer?.id -> {

                ChangeServerActivity.instance(this, server!!, selectedProtocol.name, onServerLocationChange!!)
            }


            binding?.appBar?.icLocation?.id -> {
                val intent = Intent(this, LocationActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_KEY_IP, server?.ipAddress)
                intent.putExtra(Constants.INTENT_EXTRA_KEY_SERVER_FLAG_ID, server?.flag)
                intent.putExtra("isVpnConnected", vpnConnected)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            binding?.connectButton?.id -> {
                if (binding?.vpnConnectionStatus?.text?.equals(getString(R.string.retry))!!) {

                    if (isOnline()) {

                        binding?.swipeToRefresh?.isRefreshing = true

                        ServerConfigService.listener = object : ServerConfigService.ServerConfigListener {
                            override fun serverConfigCompleted(success: Boolean) {
                                Log.e("test", "a " + success)
                                if (success) {
                                    Log.e("datalist size", DatabaseManager.serversList.size.toString())
                                    Log.e("run out", "true")
                                    runOnUiThread(Runnable {
                                        Log.e("run", "true")
                                        binding?.swipeToRefresh?.isRefreshing = false
                                        binding?.autoProtocol?.performClick()
                                        disconnectedUI()
                                    })

                                }
                                Logger.d("nnn", "Server Config Completion : $success")
                            }
                        }
                        Log.e("listner", ServerConfigService.listener.toString())
                        ServerConfigService.startService(this@HomeActivity)
                    } else
                        Toast.makeText(this, "Please enable internet", Toast.LENGTH_SHORT).show()


                } else {

                    if (binding?.connectButton?.tag == "connect") {
                        val vpnIntent = vpnManager.prepareVpn()
                        if (vpnIntent != null) {
                            startActivityForResult(vpnIntent, 111)
                        } else {
                            connectVpn()
                        }
                    } else {
                        disconnectVpn()
                    }
                }
            }

            binding?.autoProtocol?.id -> {
                selectedProtocol = ServerManager.Protocol.ALL
                server = ServerManager.getServer(activeServerProtocol(selectedProtocol))
                binding?.changeServer?.setText(server?.country)
                binding?.ivFlag?.setImageResource(server?.flag!!)
            }
            binding?.ikev2Protocol?.id -> {
                selectedProtocol = ServerManager.Protocol.IKEV2
                server = ServerManager.getServer(activeServerProtocol(selectedProtocol))
                binding?.changeServer?.setText(server?.country)
                binding?.ivFlag?.setImageResource(server?.flag!!)
            }
            binding?.tcpProtocol?.id -> {
                selectedProtocol = ServerManager.Protocol.OVPNTCP
                server = ServerManager.getServer(activeServerProtocol(selectedProtocol))
                binding?.changeServer?.setText(server?.country)
                binding?.ivFlag?.setImageResource(server?.flag!!)
            }
            binding?.udpProtocol?.id -> {
                selectedProtocol = ServerManager.Protocol.OVPNUDP
                server = ServerManager.getServer(activeServerProtocol(selectedProtocol))
                binding?.changeServer?.setText(server?.country)
                binding?.ivFlag?.setImageResource(server?.flag!!)

            }
        }
    }



   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                connectVpn()
            }
        }
    }*/
    fun openActivityForResult() {
        startForResult.launch(Intent(this, HomeActivity::class.java))
    }
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (result.resultCode == 111) {
                connectVpn()
            }
        }
    }
    private fun connectVpn() {
        binding?.connectButton?.tag = "disconnect"
        vpnManager.connect(server!!, this)
        Prefs.putLong(Constants.PREF_KEY_VPN_CONNECTED_TIMESTAMP, System.currentTimeMillis())
    }


    private fun disconnectVpn() {
        binding?.connectButton?.tag = "connect"
        vpnManager.disconnect()
    }



    private fun connectedUI() {
        binding?.ripple?.startRippleAnimation()
        binding?.ripple2?.stopRippleAnimation()
        binding?.connectButton?.visibility = View.VISIBLE
        binding?.connectButton?.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.connected_icon))
        if(isOnline())
            binding?.vpnConnectionStatus?.text = resources.getString(R.string.state_connected)
        else
            binding?.vpnConnectionStatus?.text=getString(R.string.retry)
    }

    private fun connectingUI() {
        binding?.ripple2?.startRippleAnimation()
        binding?.connectButton?.visibility = View.INVISIBLE
       binding?.vpnConnectionStatus?.text = resources.getString(R.string.state_connecting)
    }

    private fun disconnectedUI() {

        if(binding?.noserver?.visibility==View.VISIBLE) {
            binding?.noserver?.visibility = View.GONE
            binding?.changeServer?.visibility= View.VISIBLE
        }
        binding?.ripple?.stopRippleAnimation()
        binding?.ripple2?.stopRippleAnimation()
        binding?.connectButton?.visibility = View.VISIBLE
        binding?.connectButton?.setImageDrawable(ContextCompat.getDrawable(this@HomeActivity, R.drawable.disconnected_icon))
        if (isOnline())
            binding?.vpnConnectionStatus?.text = resources.getString(R.string.connect)
        else
            binding?.vpnConnectionStatus?.text=getString(R.string.retry)
    }

    override fun onStateChange(state: Int) {
        when (state) {
            VpnManager.VpnState.CONNECTED -> {
                vpnConnected = true
                connectedUI()
                if (DataManager.ADMOB_ENABLE) {
                    if (interstitialAd.isLoaded) {
                        vpnState = VpnManager.VpnState.CONNECTED
                        interstitialAd.show()
                    } else
                        openConnectSuccessActivity(VpnManager.VpnState.CONNECTED)
                } else {
                    if (interstitialAd.isLoaded) {
                        vpnState = VpnManager.VpnState.CONNECTED
                    } else
                        openConnectSuccessActivity(VpnManager.VpnState.CONNECTED)
                }
            }

            VpnManager.VpnState.CONNECTING -> {
                connectingUI()
            }
            VpnManager.VpnState.DISCONNECTED -> {
                if (DataManager.ADMOB_ENABLE) {
                    if (interstitialAd.isLoaded) {
                        vpnState = VpnManager.VpnState.DISCONNECTED
                        interstitialAd.show()
                    } else
                        openConnectSuccessActivity(VpnManager.VpnState.DISCONNECTED)
                } else {
                    if (interstitialAd.isLoaded) {
                        vpnState = VpnManager.VpnState.DISCONNECTED
                    } else
                        openConnectSuccessActivity(VpnManager.VpnState.DISCONNECTED)
                }
            }
            else -> {
                disconnectedUI()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.putString(Constants.PREF_KEY_SELECTED_VPN_CONNECTION_TAG, binding?.connectButton?.tag.toString())
        Prefs.putString(Constants.PREF_KEY_SELECTED_SERVER_IP, server?.ipAddress)
        Prefs.putLong(Constants.PREF_KEY_SELECTED_SERVER_LATENCY, server?.getLatency()!!)
        Prefs.putString(Constants.PREF_KEY_SELECTED_SERVER_COUNTRY, server?.country)
        Prefs.putInt(Constants.PREF_KEY_SELECTED_SERVER_COUNTRY_FLAG, server?.flag!!)
        Prefs.putString(Constants.PREF_KEY_SELECTED_SERVER_PROTOCOL, server?.protocol)
        ActivityManager.getInstance().onDestroy()
    }

    private fun openConnectSuccessActivity(vpnState: Int) {
        val intent = Intent(this, ConnectSuccessActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_KEY_IP, server?.ipAddress)
        intent.putExtra(Constants.INTENT_EXTRA_KEY_SERVER_COUNTRY, server?.country)
        intent.putExtra(Constants.INTENT_EXTRA_KEY_SERVER_FLAG_ID, server?.flag)
        intent.putExtra(Constants.INTENT_EXTRA_KEY_VPN_CONNECTION_STATE, vpnState)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


    private fun activeServerProtocol(protocol: ServerManager.Protocol): ServerManager.Protocol {
        binding?.ikev2Protocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        binding?.tcpProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        binding?.udpProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        binding?.autoProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorWhite))

        when (protocol) {
            ServerManager.Protocol.IKEV2 -> {
                binding?.ikev2Protocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlue))
                return ServerManager.Protocol.ALL
            }
            ServerManager.Protocol.OVPNTCP -> {
                binding?.tcpProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlue))
                return ServerManager.Protocol.OVPNTCP
            }
            ServerManager.Protocol.OVPNUDP -> {
                binding?.udpProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlue))
                return ServerManager.Protocol.OVPNUDP
            }
            else -> {
                binding?.autoProtocol?.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlue))
                return ServerManager.Protocol.ALL
            }
        }
    }


    override fun onChange(server: Server) {
        Logger.d(TAG, "Server Clicked : $server")
        this.server = server
        val protocol = when (server.protocol) {
            ServerManager.Protocol.IKEV2.name -> ServerManager.Protocol.IKEV2
            ServerManager.Protocol.OVPNTCP.name -> ServerManager.Protocol.OVPNTCP
            ServerManager.Protocol.OVPNUDP.name -> ServerManager.Protocol.OVPNUDP
            else -> ServerManager.Protocol.ALL
        }
        activeServerProtocol(protocol)
        binding?.changeServer?.setText(server?.country)
        binding?.ivFlag?.setImageResource(server?.flag!!)

        if (binding?.connectButton?.tag.toString() == "connect") {
            binding?.connectButton?.performClick()
        } else {
            binding?.connectButton?.performClick()
            object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    binding?.connectButton?.performClick()
                }
            }.start()
        }
    }

    fun isVpnActive(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return true
        var vpnInUse = false
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork: Network? = connectivityManager.activeNetwork
            val caps: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)
            try {
                return caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)!!
            } catch (e: Exception) {
                return false
            }
        }
        val networks: Array<Network> = connectivityManager.allNetworks
        for (i in networks.indices) {
            val caps: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(networks[i])
            if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)!!) {
                vpnInUse = true
                break
            }
        }
        return vpnInUse
    }


    private fun updateAvailable() {
        Log.e("updater", "called")
        val appUpdaterUtils = AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(object : UpdateListener {
                    override fun onSuccess(updated: Update, isUpdateAvailable: Boolean) {
                        Log.e("updateAvailabe", "" + isUpdateAvailable)
                        if (isUpdateAvailable) {
                            val update = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                                    .setTitle("New update available!")
                                    .setMessage("Update " + updated.latestVersion + " is available to download. By downloading the latest update you will get the latest feature, improvements and bug fixes for our team.")
                                    .setCancelable(true)
                                    .create()
                            update.show()
                        }
                    }

                    override fun onFailed(error: AppUpdaterError) {}
                })
        appUpdaterUtils.start()



    }


    fun isOnline(): Boolean {
        val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }


















}