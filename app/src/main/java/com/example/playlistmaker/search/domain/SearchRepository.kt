package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface SearchRepository {
    fun getSearchTracks(): List<Track>
    fun saveTrackInSearchHistory(track: Track): List<Track>
    fun clearSearchHistory()
}