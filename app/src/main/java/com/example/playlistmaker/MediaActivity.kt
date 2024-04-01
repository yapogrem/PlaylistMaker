package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MediaActivity : AppCompatActivity() {
    private lateinit var mediaBack: ImageButton
    private lateinit var mediaTrackImage: ImageView
    private lateinit var mediaTrackName: TextView
    private lateinit var mediaArtistName: TextView
    private lateinit var mediaButtonPlayList: ImageView
    private lateinit var mediaButtonPlay: ImageView
    private lateinit var mediaButtonFavorites: ImageView
    private lateinit var mediaTrackTime: TextView
    private lateinit var mediaTrackDuration: TextView
    private lateinit var mediaTrackAlbum: TextView
    private lateinit var mediaTrackYear: TextView
    private lateinit var mediaTrackGenre: TextView
    private lateinit var mediaTrackCountry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        mediaTrackImage = findViewById(R.id.media_track_image)
        mediaBack = findViewById(R.id.media_back)
        mediaTrackName = findViewById(R.id.media_track_name)
        mediaArtistName = findViewById(R.id.media_artist_name)
        mediaButtonPlayList = findViewById(R.id.media_button_play_list)
        mediaButtonPlay = findViewById(R.id.media_button_play)
        mediaButtonFavorites = findViewById(R.id.media_button_favorites)
        mediaTrackTime = findViewById(R.id.media_track_time)
        mediaTrackDuration = findViewById(R.id.media_track_duration)
        mediaTrackAlbum = findViewById(R.id.media_track_album)
        mediaTrackYear = findViewById(R.id.media_track_text_year)
        mediaTrackGenre = findViewById(R.id.media_track_text_genre)
        mediaTrackCountry = findViewById(R.id.media_track_country)

//Кнопка назад
        mediaBack.setOnClickListener {
            finish()
        }
fun showTrack(track: Track){
    Glide.with(mediaTrackImage)
        .load(track.artworkUrl100)
        .transform(RoundedCorners(2), FitCenter())
        .placeholder(R.drawable.placeholder)
        .into(mediaTrackImage)

}

    }

}