package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.network.StatusRequest

interface SearchCallback {
    fun changeStatus(statusRequest: StatusRequest)
}