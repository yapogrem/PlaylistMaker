package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl

object Creator {
    lateinit var app: App

    fun getExternalNavigator(): ExternalNavigator{
        return ExternalNavigatorImpl(app)
    }

    fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(app.getSharedPreferences(PLAYLIST_MAKER,
            Application.MODE_PRIVATE
        ))
    }

    fun getSearchHistoryInteractor(): SearchHistoryInteractor{
        return SearchHistoryInteractorImpl(SearchRepositoryImpl(app))
    }
}