package com.example.playlistmaker.search.domain.impl


import com.example.playlistmaker.search.data.SearchRepository

import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(
    private val searchHistory: SearchRepository
) : SearchHistoryInteractor {
    override fun clearSearchHistory() {
        searchHistory.clearSearchHistory()
    }

    override fun saveTrackToSearchHistory(track: Track): List<Track> {
        return searchHistory.saveTrackInSearchHistory(track)
    }

    override fun getHistoryTracks(): List<Track> {
        return searchHistory.getSearchTracks()
    }
}