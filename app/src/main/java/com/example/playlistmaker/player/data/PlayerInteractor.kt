package com.example.playlistmaker.player.data

interface PlayerInteractor {
    fun preparePlayer(url: String, satusObserver: StatusObserver, timerObserver: TimerObserver)
    fun start()
    fun pause()
    fun currentPosition(): String
    fun release()
}
