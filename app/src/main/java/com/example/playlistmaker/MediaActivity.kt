package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
class MediaActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
    private lateinit var mediaBack: ImageButton
    private lateinit var mediaTrackImage: ImageView
    private lateinit var mediaTrackName: TextView
    private lateinit var mediaArtistName: TextView
    private lateinit var mediaTrackDuration: TextView
    private lateinit var mediaTrackTime: TextView
    private lateinit var mediaTrackAlbum: TextView
    private lateinit var mediaTrackYear: TextView
    private lateinit var mediaTrackGenre: TextView
    private lateinit var mediaTrackCountry: TextView
    private lateinit var mediaButtonPlay: ImageView
    private lateinit var timer: MediaPlayTimer
    private var mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        mediaBack = findViewById(R.id.media_back)
        mediaTrackImage = findViewById(R.id.media_track_image)
        mediaTrackName = findViewById(R.id.media_track_name)
        mediaArtistName = findViewById(R.id.media_artist_name)
        mediaTrackDuration = findViewById(R.id.media_track_duration)
        mediaTrackTime = findViewById(R.id.media_track_time)
        mediaTrackAlbum = findViewById(R.id.media_track_album)
        mediaTrackYear = findViewById(R.id.media_track_year)
        mediaTrackGenre = findViewById(R.id.media_track_genre)
        mediaTrackCountry = findViewById(R.id.media_track_country)
        mediaButtonPlay = findViewById(R.id.media_button_play)
        mediaButtonPlay.setImageResource(R.drawable.media_button_play)

        timer= MediaPlayTimer(mediaTrackTime, mediaPlayer)

        mediaButtonPlay.setOnClickListener {
            playbackControl()
        }

        //Кнопка назад
        mediaBack.setOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra("track") as Track
        preparePlayer(track)
        showTrack(track)
    }

    private fun showTrack(track: Track) {
        Glide.with(mediaTrackImage)
            .load(track.artworkUrl100?.let { getCoverArtwork(it) })
            .transform(RoundedCorners(2), FitCenter())
            .placeholder(R.drawable.placeholder)
            .into(mediaTrackImage)
        mediaTrackName.text = track.trackName
        mediaArtistName.text = track.artistName
        mediaTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        mediaTrackAlbum.text = track.collectionName
        mediaTrackYear.text = track.releaseDate?.let { getYear(it).toString() }
        mediaTrackGenre.text = track.primaryGenreName
        mediaTrackCountry.text = track.country

    }
    private fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    private fun getYear(releaseDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val convertedDate = LocalDateTime.parse(releaseDate, formatter)
        return convertedDate.year
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
            mediaButtonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mediaButtonPlay.setImageResource(R.drawable.media_button_play)
            playerState = STATE_PREPARED
            timer.stop()
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        mediaButtonPlay.setImageResource(R.drawable.media_button_pause)
        playerState = STATE_PLAYING
        timer.start()
    }
    private fun pausePlayer() {
        mediaPlayer.pause()
        mediaButtonPlay.setImageResource(R.drawable.media_button_play)
        playerState = STATE_PAUSED
        timer.stop()
    }
}
