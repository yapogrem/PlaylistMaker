package com.example.playlistmaker.search.data.network
import com.example.playlistmaker.search.domain.SearchCallback
import com.example.playlistmaker.search.domain.models.Track



interface SearchTrackRepository {
    fun getTracksRequest(inputSearch:String, callback: SearchCallback):List<Track>
}