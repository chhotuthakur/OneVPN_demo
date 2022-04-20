
package com.one.hotspot.vpn.free.master.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.hotspot.vpn.free.master.adapter.HelpQAAdapter
import com.one.hotspot.vpn.free.master.util.Constants
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.ActivityHelpBinding


class HelpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider)!!)
        binding.helpRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.helpRecyclerView.adapter = HelpQAAdapter(Constants.getHelpQA(this))
        binding.helpRecyclerView.addItemDecoration(itemDecoration)
    }
}