package com.example.playlistmaker.search.domain.impl

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchRepositoryImpl(private val sharedPrefs: SharedPreferences, private val gson: Gson) :
    SearchRepository {

    private val searchHistoryList: LinkedList<Track>


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
            .putString(SEARCH_HISTORY_KEY, gson.toJson(searchHistoryList))
            .apply()
        return searchHistoryList
    }

    override fun clearSearchHistory() {
        searchHistoryList.clear()
        sharedPrefs.edit().clear().apply()
    }

    private fun deserialaizer(json: String): LinkedList<Track> {
        val type: Type = object : TypeToken<LinkedList<Track>>() {}.type
        return gson.fromJson(json, type)
    }
}