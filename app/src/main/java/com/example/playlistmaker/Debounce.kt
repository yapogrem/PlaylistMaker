package com.example.playlistmaker

import android.os.Handler
import android.os.Looper

class Debounce {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    fun searchDebounce(searchRunnable: Runnable) {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
    fun canselSearchDebounce(searchRunnable: Runnable) {
        handler.removeCallbacks(searchRunnable)
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}