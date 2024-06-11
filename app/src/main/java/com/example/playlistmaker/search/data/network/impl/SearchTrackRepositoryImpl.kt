package com.example.playlistmaker.search.data.network.impl

import com.example.playlistmaker.search.data.TracksResponse
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.StatusRequest
import com.example.playlistmaker.search.domain.SearchCallback
import com.example.playlistmaker.search.domain.models.Track
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchTrackRepositoryImpl : SearchTrackRepository {


    private val iTunesService: ITunesApi
    private var tracks = ArrayList<Track>()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val iTunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        iTunesService = retrofit.create(ITunesApi::class.java)

    }

    override fun getTracksRequest(inputSearch: String, callback: SearchCallback): List<Track> {
        tracks.clear()
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