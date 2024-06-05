package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.data.Debounce
import com.example.playlistmaker.player.ui.PlayerActivity



class SearchHistoryAdapter(private val viewModel: SearchViewModel) : RecyclerView.Adapter<TrackViewHolder> () {
    private val debounce = Debounce(viewModel)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = viewModel.getTrackByPositionHistory(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            if (debounce.clickDebounce()) {
                viewModel.saveTrackInSearchHistory(track)
                notifyDataSetChanged()
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra("track", track)
                holder.itemView.context.startActivity(intent)

            }
        }
    }
    override fun getItemCount():Int = viewModel.getCountHistory()
}
