package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView

class MediaPlayTimer(private val textView: TextView, private val mediaPlayer: MediaPlayer) {
    companion object {
        private const val DELAY = 1000L
    }

    private val timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            var seconds = mediaPlayer.currentPosition / DELAY
            val minutes = seconds / 60
            seconds %= 60
            textView.text = String.format("%d:%02d", minutes, seconds)
            timerHandler.postDelayed(this, DELAY)
        }
    }

    fun start() {
        timerHandler.postDelayed(timerRunnable, DELAY)
    }

    fun stop() {
        timerHandler.removeCallbacks(timerRunnable)
    }
}



