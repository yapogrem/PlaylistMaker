package com.example.playlistmaker

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
    private lateinit var mediaBack: ImageButton
    private lateinit var mediaTrackImage: ImageView
    private lateinit var mediaTrackName: TextView
    private lateinit var mediaArtistName: TextView

    //    private lateinit var mediaButtonPlayList: ImageView
//    private lateinit var mediaButtonPlay: ImageView
//    private lateinit var mediaButtonFavorites: ImageView
//    private lateinit var mediaTrackTime: TextView
    private lateinit var mediaTrackDuration: TextView
    private lateinit var mediaTrackAlbum: TextView
    private lateinit var mediaTrackYear: TextView
    private lateinit var mediaTrackGenre: TextView
    private lateinit var mediaTrackCountry: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        mediaBack = findViewById(R.id.media_back)
        mediaTrackImage = findViewById(R.id.media_track_image)
        mediaTrackName = findViewById(R.id.media_track_name)
        mediaArtistName = findViewById(R.id.media_artist_name)
//        mediaButtonPlayList = findViewById(R.id.media_button_play_list)
//        mediaButtonPlay = findViewById(R.id.media_button_play)
//        mediaButtonFavorites = findViewById(R.id.media_button_favorites)
//        mediaTrackTime = findViewById(R.id.media_track_time)
        mediaTrackDuration = findViewById(R.id.media_track_duration)
        mediaTrackAlbum = findViewById(R.id.media_track_album)
        mediaTrackYear = findViewById(R.id.media_track_year)
        mediaTrackGenre = findViewById(R.id.media_track_genre)
        mediaTrackCountry = findViewById(R.id.media_track_country)

        //Кнопка назад
        mediaBack.setOnClickListener {
            finish()
        }

        val track = intent.getSerializableExtra("track") as Track
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
}