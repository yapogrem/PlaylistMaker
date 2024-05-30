package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.App
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

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

    fun getSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl()
    }
    fun getSettingsInteractorImpl(): SettingsInteractor {
        return SettingsInteractorImpl()
    }

}