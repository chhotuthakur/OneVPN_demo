

package com.one.hotspot.vpn.free.master.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.one.hotspot.vpn.free.master.manager.ServerManager
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.util.vpn.server.Server
import com.one.hotspot.vpn.free.master.R


class ChangeCountryAdapter(val countryList: HashMap<String, MutableList<Server>>, val selectedServer: Server, val selectedProtocol: String, val listener: InnerServerAdapter.ViewHolder.ServerListener) : RecyclerView.Adapter<ChangeCountryAdapter.ViewHolder>() {

    private val keysList = ArrayList(countryList.keys)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView: View = LayoutInflater.from(parent.context).inflate(R.layout.country_list_item, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = keysList[position]
        holder.bindView(country, countryList[country]!!, selectedServer, selectedProtocol, listener)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    private fun refreshList(country: String) {
        ViewHolder.adapter.updateList(ServerManager.getCountryServerList(country)?.sortedWith(compareBy { it.getLatency() })!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            lateinit var adapter: InnerServerAdapter
        }

        private val countryLayout: LinearLayout = itemView.findViewById(R.id.country_layout)
        private val countryTextView: TextView = itemView.findViewById(R.id.country_name)
        private val selectedIcon: ImageView = itemView.findViewById(R.id.selected_icon)
        private val countryFlagIcon: ImageView = itemView.findViewById(R.id.country_flag)
        private val countryServerSignal: ImageView = itemView.findViewById(R.id.signalLevel)
        private val serverRecyclerView: RecyclerView = itemView.findViewById(R.id.server_recycler_view)


        fun bindView(country: String, servers: MutableList<Server>, selectedServer: Server, selectedProtocol: String, listener: InnerServerAdapter.ViewHolder.ServerListener) {
            countryTextView.text = country
            if (selectedServer.country == country) {
                selectedIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_choice_selected))
            }
            val latency = Constants.getAverageLatencyByCountry(servers)
            Constants.setPingTextSignal(itemView.context, countryServerSignal, latency)

            countryFlagIcon.setImageResource(servers[0].flag)
            val serverList = servers.sortedWith(compareBy { it.getLatency() })
            serverRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            adapter = InnerServerAdapter(serverList, listener, selectedServer, selectedProtocol)
            serverRecyclerView.adapter = adapter

            countryLayout.setOnClickListener {
                if (innerServerListIsExpanded()) {
                    serverRecyclerView.visibility = View.GONE
                    changeRightDrawable(countryTextView, R.drawable.ic_keyboard_arrow_down)
                } else {
                    serverRecyclerView.visibility = View.VISIBLE
                    changeRightDrawable(countryTextView, R.drawable.ic_keyboard_arrow_up)
                }
            }
        }


        private fun changeRightDrawable(textView: TextView, drawable: Int) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
        }

        private fun innerServerListIsExpanded(): Boolean {
            return serverRecyclerView.visibility == View.VISIBLE
        }
    }
}