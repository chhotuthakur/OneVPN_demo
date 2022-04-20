
package com.one.hotspot.vpn.free.master.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.hotspot.vpn.free.master.DataManager

import com.one.hotspot.vpn.free.master.adapter.ChangeCountryAdapter
import com.one.hotspot.vpn.free.master.adapter.InnerServerAdapter
import com.one.hotspot.vpn.free.master.manager.ServerManager
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.Logger
import com.one.hotspot.vpn.free.master.util.service.ServerConfigService
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.ActivityChangeServerBinding

import java.io.Serializable


class ChangeServerActivity : AppCompatActivity(), View.OnClickListener, InnerServerAdapter.ViewHolder.ServerListener {

    private var paddingAdded = false
    private lateinit var adapter: ChangeCountryAdapter
    private lateinit var binding: ActivityChangeServerBinding
    private lateinit var activity: Activity
    private lateinit var serverListener: InnerServerAdapter.ViewHolder.ServerListener


    companion object {
        private const val TAG = "ChangeServerActivity"
        private var onServerLocationChange: OnServerLocationChange? = null
        var server: Server? = null
        private var selectedProtocol: String? = null
        fun instance(activity: Activity, server: Server, selectedProtocol: String, onServerLocationChange: OnServerLocationChange) {
            ChangeServerActivity.onServerLocationChange = onServerLocationChange
            ChangeServerActivity.server = server
            ChangeServerActivity.selectedProtocol = selectedProtocol
            val intent = Intent(activity, ChangeServerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this
        serverListener = this
        binding.backIcon.setOnClickListener(this)
        binding.refreshButton.setOnClickListener(this)

        binding.swipeToRefresh.setOnRefreshListener {
            binding.refreshButton.performClick()
        }
        setupLocations()
    }

    private fun setupLocations() {
        adapter = ChangeCountryAdapter(groupAllServersByCountry(), server!!, selectedProtocol!!, this)
        binding.serverRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.serverRecyclerView.adapter = adapter
        if (!paddingAdded) {
            val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_tiny)!!)
            binding.serverRecyclerView.addItemDecoration(itemDecoration)
            paddingAdded = true
        }

    }

    private fun groupAllServersByCountry(): HashMap<String, MutableList<Server>> {
        val map: HashMap<String, MutableList<Server>> = HashMap<String, MutableList<Server>>()
        for (server in ServerManager.getTotalServers()) {
            val key: String = server.country
            if (map.containsKey(key)) {
                val list: MutableList<Server> = map[key]!!
                list.add(server)
            } else {
                val list: MutableList<Server> = ArrayList<Server>()
                list.add(server)
                map[key] = list
            }
        }

        val maps = Constants.sortHashMapByValue(map)
        val iterator = maps.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            Logger.d(TAG, key)
        }
        return maps
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.backIcon.id -> onBackPressed()
            binding.refreshButton.id -> {
                binding.swipeToRefresh.isRefreshing = true

                ServerConfigService.listener = object : ServerConfigService.ServerConfigListener {
                    override fun serverConfigCompleted(success: Boolean) {
                        if (success) {
                            binding.swipeToRefresh.isRefreshing = false
                            try {
                                activity.runOnUiThread {
                                    setupLocations()
                                }
                            } catch (ignore: Exception) {

                            }
                        }
                        Logger.d(TAG, "Server Config Completion : $success")
                    }
                }
                ServerConfigService.startService(this)
            }
        }
    }

    override fun onServerClick(server: Server) {
        if(server?.isPremium.equals("2")){

            if (DataManager.free_or_paid){
                onServerLocationChange?.onChange(server)
                onBackPressed()

            }else{
                val dialog = Premium_dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                dialog.show()

            }




        }else{
            onServerLocationChange?.onChange(server)
            onBackPressed()
        }

    }

    interface OnServerLocationChange : Serializable {
        fun onChange(server: Server)
    }

}