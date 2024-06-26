package com.example.playlistmaker.search.data

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.search.ui.SearchViewModel

class Debounce(private val viewModel: SearchViewModel) {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    init {
        searchRunnable = Runnable { viewModel.findTrack("") }
    }
    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    fun searchDebounce(searchString: String) {
        searchRunnable?.let {
            handler.removeCallbacks(it)
            searchRunnable = Runnable { viewModel.findTrack(searchString) }
            handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
        }
    }
    fun canselSearchDebounce() {
        searchRunnable?.let {
            handler.removeCallbacks(it)
        }
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 3000L
    }
}