package com.example.playlistmaker.creator

import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.dto.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator

object Creator {
    lateinit var app: App
    fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(app)
    }

    fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(app)
    }

    fun getSettingsIterator(): SettingsInteractor {
        return SettingsInteractorImpl()
    }

    fun getSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(SearchRepositoryImpl(app))
    }
}