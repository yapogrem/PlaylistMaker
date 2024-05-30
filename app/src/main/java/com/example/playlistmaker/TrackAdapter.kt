package com.example.playlistmaker

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.Debounce
import com.example.playlistmaker.player.ui.MediaActivity


class TrackAdapter(private val searchHistory: SearchHistory) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    private val debounce = Debounce()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.itemView.setOnClickListener {
            if (debounce.clickDebounce()) {
            searchHistory.saveTrackInSearchHistory(track)
            val intent = Intent(holder.itemView.context, MediaActivity::class.java)
            intent.putExtra("track", track)
            holder.itemView.context.startActivity(intent)
            }
        }
        holder.bind(tracks[position])
    }
    override fun getItemCount():Int = tracks.size
}
