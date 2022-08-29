package com.dev.eventapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.eventapp.databinding.ItemEventListBinding
import com.dev.eventapp.models.Event

class EventAdapter(private val events : List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(ItemEventListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.holdData(event)
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: ItemEventListBinding) : RecyclerView.ViewHolder(binding.root){
        fun holdData(event: Event) {
            binding.tvEventName.text = event.title
            binding.tvDate.text = event.date
            binding.tvInstruactorName.text = event.instructorName
            binding.tvDescription.text = event.description
        }
    }
}