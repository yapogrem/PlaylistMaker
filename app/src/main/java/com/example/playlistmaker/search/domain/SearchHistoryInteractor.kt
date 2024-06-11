package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun clearSearchHistory()
    fun saveTrackToSearchHistory(track: Track): List<Track>
    fun getHistoryTracks(): List<Track>
}