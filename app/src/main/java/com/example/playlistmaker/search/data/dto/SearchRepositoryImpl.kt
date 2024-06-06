package com.example.playlistmaker.search.data.dto

import android.app.Application
import android.content.Context
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchRepositoryImpl(application:Application) : SearchRepository {

    private val searchHistoryList: LinkedList<Track>

    private val sharedPrefs = application.getSharedPreferences(
        PLAYLIST_MAKER,
        Context.MODE_PRIVATE
    )


    init {
        val json = sharedPrefs.getString(SEARCH_HISTORY_KEY, null)
        searchHistoryList = if (json == null) {
            LinkedList()
        } else {
            deserialaizer(json)
        }
    }

    override fun getSearchTracks(): List<Track> {
        return searchHistoryList
    }

    override fun saveTrackInSearchHistory(track: Track): List<Track> {
        searchHistoryList.remove(track)
        if (searchHistoryList.size == 10) {
            searchHistoryList.removeLast()
        }
        searchHistoryList.addFirst(track)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(searchHistoryList))
            .apply()
        return searchHistoryList
    }

    override fun clearSearchHistory() {
        searchHistoryList.clear()
        sharedPrefs.edit().clear().apply()
    }

    private fun deserialaizer(json: String): LinkedList<Track> {
        val type: Type = object : TypeToken<LinkedList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }
}