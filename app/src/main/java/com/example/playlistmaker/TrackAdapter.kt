package com.example.playlistmaker
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


class TrackAdapter(val searchHistory: SearchHistory) : RecyclerView.Adapter<TrackViewHolder> () {
    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.itemView.setOnClickListener {
            searchHistory.saveTrackInSearchHistory(track)
        }
        holder.bind(tracks[position])
    }
    override fun getItemCount():Int = tracks.size
}
