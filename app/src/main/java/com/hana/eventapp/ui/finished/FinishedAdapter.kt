package com.hana.eventapp.ui.finished

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hana.eventapp.data.response.ListEventsItem
import com.hana.eventapp.databinding.FinishedItemBinding

class FinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, FinishedAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: FinishedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            // Set text values
            binding.tvName.text = event.name
            binding.locationValue.text = event.cityName
            binding.quotaValue.text = event.quota.toString()
            binding.registrantValue.text = event.registrants.toString()

            // Load image using Glide with caching strategy
            Glide.with(binding.ivFinished.context)
                .load(event.imageLogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // Cache all versions (original and transformed)
                .placeholder(android.R.color.darker_gray)  // Default placeholder during loading
                .error(android.R.color.holo_red_light)     // Error placeholder if the image fails to load
                .into(binding.ivFinished)

            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FinishedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id  // Ensure comparison by ID
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem  // Ensure content equality comparison
            }
        }
    }
}
