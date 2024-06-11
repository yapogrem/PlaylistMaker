package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.Debounce


class TrackAdapter(private val viewModel: SearchViewModel) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private val debounce = Debounce(viewModel)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = viewModel.getTrackByPositionSearch(position)
        holder.itemView.setOnClickListener {
            if (debounce.clickDebounce()) {
            viewModel.saveTrackInSearchHistory(track)
            val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
            intent.putExtra("track", track)
            holder.itemView.context.startActivity(intent)
            }
        }
        holder.bind(track)
    }
    override fun getItemCount():Int = viewModel.getSizeSearch()
}
