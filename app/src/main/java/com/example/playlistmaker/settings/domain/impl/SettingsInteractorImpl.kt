package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl : SettingsInteractor {
    private val settingsRepository: SettingsRepository = Creator.getSettingsRepository()
    override fun getThemeSettings(isDark: Boolean): Boolean {
        return settingsRepository.getThemeSettings(isDark)
    }
    override fun updateThemeSettings(isDark: Boolean){
        settingsRepository.updateThemeSettings(isDark)
    }
}