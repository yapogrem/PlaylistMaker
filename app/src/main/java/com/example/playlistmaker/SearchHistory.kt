package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.LinkedList

const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val searchHistoryList: LinkedList<Track>

    init {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        searchHistoryList = if (json == null) {
            LinkedList()
        } else {
            deserialaizer(json)
        }
    }

    fun saveTrackInSearchHistory(track: Track) {
        searchHistoryList.remove(track)
        if (searchHistoryList.size == 10) {
            searchHistoryList.removeLast()
        }
        searchHistoryList.addFirst(track)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(searchHistoryList))
            .apply()
    }

    fun clearSearchHistory() {
        searchHistoryList.clear()
        sharedPreferences.edit().clear().apply()
    }

    fun getTrackByPosition(position: Int): Track {
        return searchHistoryList[position]
    }

    fun getItemCount(): Int {
        return searchHistoryList.size
    }

    private fun deserialaizer(json: String): LinkedList<Track> {
        val type: Type = object : TypeToken<LinkedList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }
}