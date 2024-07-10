package com.example.playlistmaker.search.data.network.impl

import com.example.playlistmaker.search.data.TracksResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.StatusRequest
import com.example.playlistmaker.search.domain.SearchCallback
import com.example.playlistmaker.search.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTrackRepositoryImpl(private val iTunesService: ITunesApi) : SearchTrackRepository {


    override fun getTracksRequest(inputSearch: String, callback: SearchCallback): List<Track> {
        val tracks= ArrayList<Track>()
        iTunesService.searchTrack(inputSearch)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true){
                            tracks.addAll(response.body()?.results!!.map { Track.mapped(it) })
                            callback.changeStatus(StatusRequest.REQUEST_OK)
                        } else {
                            callback.changeStatus(StatusRequest.REQUEST_EMPTY)
                        }
                    } else {
                        callback.changeStatus(StatusRequest.REQUEST_SERVER_ERROR)
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    callback.changeStatus(StatusRequest.REQUEST_NETWORK_ERROR)
                }
            })
         return tracks
    }
}