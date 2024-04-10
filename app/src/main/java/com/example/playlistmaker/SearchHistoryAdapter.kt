package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchHistoryAdapter(private val searchHistory: SearchHistory) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = searchHistory.getTrackByPosition(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            searchHistory.saveTrackInSearchHistory(track)
            val intent = Intent(holder.itemView.context, MediaActivity::class.java)
            intent.putExtra("track", track)
            holder.itemView.context.startActivity(intent)
            notifyDataSetChanged()
        }
    }
    override fun getItemCount():Int = searchHistory.getItemCount()
}
