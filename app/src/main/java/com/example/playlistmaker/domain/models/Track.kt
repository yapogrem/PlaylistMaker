package com.example.playlistmaker.domain.models

import com.example.playlistmaker.data.dto.TrackDto
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class Track (
    var artistName: String? = null,
    var collectionName: String? = null,
    var trackName: String? = null,
    var artworkUrl100: String? = null,
    var trackTimeMillis: String? = null,
    var country: String? = null,
    var primaryGenreName: String? = null,
    var releaseDate: String? = null,
    var previewUrl: String? =null,
    ): Serializable{
        companion object{
            fun mapped(dto:TrackDto):Track{
                val track = Track()
                track.artistName = dto.artistName
                track.collectionName = dto.collectionName
                track.trackName = dto.trackName
                track.artworkUrl100 = getCoverArtwork(dto.artworkUrl100)
                track.trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(dto.trackTimeMillis)
                track.country = dto.country
                track.primaryGenreName = dto.primaryGenreName
                track.releaseDate = getYear(dto.releaseDate).toString()
                track.previewUrl = dto.previewUrl
                return track
            }
            private fun getYear(releaseDate: Date?): String {
                val trackYear =  when {
                    releaseDate == null -> ""
                    else -> {
                        releaseDate.toInstant().atZone(ZoneId.of("UTC"))
                            .year.toString()
                    }
                }
                    return trackYear

            }
            private fun getCoverArtwork(artworkUrl100: String?) =
                artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        }


    }

