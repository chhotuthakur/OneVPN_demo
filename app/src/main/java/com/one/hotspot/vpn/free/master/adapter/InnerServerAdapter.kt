
package com.one.hotspot.vpn.free.master.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.ui.ChangeServerActivity
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.vpn.server.Server

class InnerServerAdapter(
        private var serverList: List<Server>,
        private val listener: ViewHolder.ServerListener,
        private val selectedServer: Server,
        private val selectedProtocol: String
) : RecyclerView.Adapter<InnerServerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView: View = LayoutInflater.from(parent.context).inflate(R.layout.server_location_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(serverList[position], listener, selectedServer, selectedProtocol)
    }

    override fun getItemCount(): Int {
        return serverList.size
    }

    fun updateList(list: List<Server>) {
        this.serverList = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val serverLayout: LinearLayout = itemView.findViewById(R.id.server_layout)
        private val serverName: TextView = itemView.findViewById(R.id.server_country_name)
        private val countryServerSignal: ImageView = itemView.findViewById(R.id.signalLevel)
        private val serverSelectedIcon: ImageView = itemView.findViewById(R.id.selected_icon)
        private val serverCountryFlag: ImageView = itemView.findViewById(R.id.server_country_flag)
        private val vip: ImageView = itemView.findViewById(R.id.vip)

        init {
            serverLayout.setPadding(64, 32, 24, 32)
            serverName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            serverName.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.inner_server_name_text_size))
        }

        fun bindView(server: Server, listener: ServerListener, selectedServer: Server, selectedProtocol: String) {
            Constants.setPingTextSignal(itemView.context, countryServerSignal, server.getLatency())
            serverName.text = server.city
            try {
                serverCountryFlag.setImageDrawable(ContextCompat.getDrawable(itemView.context, server.flag))
            } catch (e: Exception) {
            }

            if (server.ipAddress == selectedServer.ipAddress && selectedProtocol == selectedServer.protocol) {
                serverSelectedIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_choice_selected))
            }

            serverLayout.setOnClickListener {
                listener.onServerClick(server)
            }

            if (server.isPremium.equals("2")) {
                vip.setVisibility(View.VISIBLE)
            } else {
                vip.setVisibility(View.GONE)
            }
        }

        interface ServerListener {
            fun onServerClick(server: Server)
        }
    }

}