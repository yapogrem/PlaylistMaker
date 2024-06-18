package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsDiModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}