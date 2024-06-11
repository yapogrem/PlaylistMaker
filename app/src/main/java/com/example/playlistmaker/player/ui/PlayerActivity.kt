package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.player.data.PlayerState
import com.example.playlistmaker.player.data.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.models.Track

class PlayerActivity : AppCompatActivity() {


    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityMediaBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val track = intent.getSerializableExtra("track") as Track

        viewModel = ViewModelProvider(
            this, PlayerViewModel.getViewModelFactory(
                track.previewUrl!!,
                PlayerInteractorImpl(),
            )
        )[PlayerViewModel::class.java]

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)


        binding.mediaButtonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        //Кнопка назад
        binding.mediaBack.setOnClickListener {
            finish()
        }


        showTrack(track)

        viewModel.observeState().observe(this){
            when(it){
                PlayerState.STATE_PLAYING -> {
                    binding.mediaButtonPlay.setImageResource(R.drawable.media_button_pause)
                }

                PlayerState.STATE_COMPLETE -> {
                    binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)
                    binding.mediaTrackTime.text = "0:00"
                }
                PlayerState.STATE_PAUSED->{
                    binding.mediaButtonPlay.setImageResource(R.drawable.media_button_play)
                }
            }
        }
        viewModel.observeTimer().observe(this){
            binding.mediaTrackTime.text=it
        }
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
        viewModel.pause()
    }
}
