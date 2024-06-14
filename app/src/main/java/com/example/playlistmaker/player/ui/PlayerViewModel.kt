package com.example.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.data.PlayerInteractor
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.data.StatusObserver
import com.example.playlistmaker.player.data.TimerObserver
import com.example.playlistmaker.player.data.impl.PlayerInteractorImpl


class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val statePlayerLiveData = MutableLiveData(PlayerState.STATE_COMPLETE)
    fun observeState(): LiveData<PlayerState> = statePlayerLiveData

    private val timerPlayerLiveData = MutableLiveData<String>()
    fun observeTimer(): LiveData<String> = timerPlayerLiveData


    fun start() {
        playerInteractor.start()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun playbackControl() {
        val  value = statePlayerLiveData.value ?: return

        when (value) {
            PlayerState.STATE_PLAYING -> {
                pause()
            }
            PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                start()
            }
        }
    }

    fun prepare(url: String?) {
        if (url != null) {
            playerInteractor.preparePlayer(url, statusObserver, timerObserver)
        }
    }

    val statusObserver = object: StatusObserver {
        override fun onStop() {
            statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
        }

        override fun onPlay() {
            statePlayerLiveData.postValue(PlayerState.STATE_PLAYING)
        }

        override fun onComplete() {
            statePlayerLiveData.postValue(PlayerState.STATE_COMPLETE)
        }

    }
    val timerObserver = object: TimerObserver{
        override fun updateTimer(value: Int) {
            var seconds = value / PlayerInteractorImpl.DELAY
            val minutes = seconds / 60
            seconds %= 60
            val timer = String.format("%d:%02d", minutes, seconds)
            timerPlayerLiveData.postValue(timer)
        }

    }


}
