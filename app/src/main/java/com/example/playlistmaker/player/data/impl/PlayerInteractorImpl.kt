package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.player.data.PlayerInteractor
import com.example.playlistmaker.player.data.StatusObserver
import com.example.playlistmaker.player.data.TimerObserver
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl : PlayerInteractor {
    companion object {
        const val DELAY = 300L
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            timerObserver.updateTimer(player!!.currentPosition)
            timerHandler.postDelayed(this, DELAY)
        }
    }

    var player: MediaPlayer? = null
    private lateinit var sratusObserver: StatusObserver
    private lateinit var timerObserver: TimerObserver


    override fun preparePlayer(url: String, satusObserver: StatusObserver, timerObserver: TimerObserver) {
        player = MediaPlayer()
        player!!.setDataSource(url)
        player!!.prepareAsync()
        this.sratusObserver = satusObserver
        player!!.setOnCompletionListener {
            timerHandler.removeCallbacks(timerRunnable)
            satusObserver.onComplete()
        }

        this.timerObserver = timerObserver
    }

    override fun start() {
        player!!.start()
        sratusObserver.onPlay()
        timerHandler.postDelayed(timerRunnable, DELAY)
    }

    override fun pause() {
        player!!.pause()
        sratusObserver.onStop()
        timerHandler.removeCallbacks(timerRunnable)
    }

    override fun currentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(player!!.currentPosition)
    }

    override fun release() {
        player!!.release()
        player = null
        timerHandler.removeCallbacks(timerRunnable)
    }
}