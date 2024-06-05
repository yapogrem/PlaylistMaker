package com.example.playlistmaker.player.data

interface StatusObserver {
    fun onStop()
    fun onPlay()
    fun onComplete()
}