package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.MediaPlayTimer

class MediaActivity : ComponentActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private lateinit var viewModel: MediaViewModel
    private lateinit var binding: ActivityMediaBinding
    private var playerState = STATE_DEFAULT
    private lateinit var timer: MediaPlayTimer
    private var mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MediaViewModel::class.java]

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)

        timer= MediaPlayTimer(binding.mediaTrackTime, mediaPlayer)

        binding.mediaButtonPlay.setOnClickListener {
            playbackControl()
        }

        //Кнопка назад
        binding.mediaBack.setOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra("track") as Track
        preparePlayer(track)
        showTrack(track)
    }

    private fun showTrack(track: Track) {
        Glide.with(binding.mediaTrackImage)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2), FitCenter())
            .placeholder(R.drawable.placeholder)
            .into(binding.mediaTrackImage)
        binding.mediaTrackName.text = track.trackName
        binding.mediaArtistName.text = track.artistName
        binding.mediaTrackDuration.text = track.trackTimeMillis
        binding.mediaTrackAlbum.text = track.collectionName
        binding.mediaTrackYear.text = track.releaseDate.toString()
        binding.mediaTrackGenre.text = track.primaryGenreName
        binding.mediaTrackCountry.text = track.country
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.mediaButtonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)
            playerState = STATE_PREPARED
            timer.stop()
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        binding.mediaButtonPlay.setImageResource(R.drawable.media_button_pause)
        playerState = STATE_PLAYING
        timer.start()
    }
    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)
        playerState = STATE_PAUSED
        timer.stop()
    }
}
