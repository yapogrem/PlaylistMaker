package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.StatusRequest
import com.example.playlistmaker.search.domain.SearchCallback
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.model.SearchState

class SearchViewModel(
    private val searchInteractor: SearchHistoryInteractor,
    private var searchState: SearchTrackRepository,
) : ViewModel() {

    private var tracks:List<Track> = ArrayList()
    private var historyTracks: List<Track> = searchInteractor.getHistoryTracks()
    private var screenStateLiveData = MutableLiveData(SearchState.SEARCH_HISTORY)

    private val searchCallback: SearchCallback = object : SearchCallback {
        override fun changeStatus(statusRequest: StatusRequest) {
            when (statusRequest) {
                StatusRequest.REQUEST_START -> screenStateLiveData.value = SearchState.PROGRESSBAR
                StatusRequest.REQUEST_OK -> screenStateLiveData.value = SearchState.SEARCH_RESULTS
                StatusRequest.REQUEST_EMPTY -> screenStateLiveData.value = SearchState.NOTHING_FOUND
                StatusRequest.REQUEST_NETWORK_ERROR -> screenStateLiveData.value =
                    SearchState.SEARCH_ERROR
                StatusRequest.REQUEST_SERVER_ERROR -> SearchState.SEARCH_ERROR
            }
        }
    }

    fun getScreenStateLiveData(): LiveData<SearchState> = screenStateLiveData

    fun findTrack(inputSearch: String) {
        tracks = searchState.getTracksRequest(inputSearch,searchCallback)
    }

    fun getCountHistory(): Int {
        return historyTracks.size
    }

    fun getTrackByPositionHistory(position: Int): Track {
        return historyTracks[position]
    }

    fun saveTrackInSearchHistory(track: Track) {
        this.historyTracks = searchInteractor.saveTrackToSearchHistory(track)
    }

    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        historyTracks = emptyList()

    }

    fun getTrackByPositionSearch(position: Int): Track {
        return tracks[position]
    }

    fun getSizeSearch(): Int {
        return tracks.size
    }
}