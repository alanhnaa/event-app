package com.hana.eventapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hana.eventapp.data.response.ListEventsItem
import com.hana.eventapp.databinding.HomeFinishedItemBinding

class HomeFinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, HomeFinishedAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: HomeFinishedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvName.text = event.name
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.ivCarousel)
            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeFinishedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }
}