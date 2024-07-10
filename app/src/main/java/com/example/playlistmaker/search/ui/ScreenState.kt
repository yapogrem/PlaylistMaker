package com.example.playlistmaker.search.ui

import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.FragmentFindBinding

class ScreenState(private val binding: FragmentFindBinding) {

    fun showNetworkError() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = true
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
    fun showItemsNoFound() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = true
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
    fun showProgressBar() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = true
    }
    fun showEmptyScreen() {
        binding.recyclerViewTrack.isVisible = false
        binding.networkError.isVisible = false
        binding.itemsNotFound.isVisible = false
        binding.searchHistory.isVisible = false
        binding.findProgressBar.isVisible = false
    }
}