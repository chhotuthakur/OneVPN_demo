
package com.one.hotspot.vpn.free.master.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.one.hotspot.vpn.free.master.model.HelpQA
import com.one.hotspot.vpn.free.master.R
import com.one.hotspot.vpn.free.master.databinding.HelpListItemBinding


class HelpQAAdapter(private var qaList: List<HelpQA>) : RecyclerView.Adapter<HelpQAAdapter.ViewHolder>() {

    private lateinit var binding: HelpListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HelpListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(qaList[position])
    }

    override fun getItemCount(): Int {
        return qaList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val questionTextView: TextView = itemView.findViewById(R.id.question_text_view)
        private val answerTextView: TextView = itemView.findViewById(R.id.answer_text_view)

        fun bindView(helpQA: HelpQA) {
            questionTextView.text = helpQA.question
            answerTextView.text = helpQA.answer
        }
    }

}