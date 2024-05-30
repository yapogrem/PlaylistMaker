package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.data.SettingsRepository

class SettingsInteractorImpl : SettingsInteractor {
    private val settingsRepository: SettingsRepository = Creator.getSettingsRepository()
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }
}