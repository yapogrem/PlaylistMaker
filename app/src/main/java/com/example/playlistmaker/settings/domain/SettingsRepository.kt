package com.example.playlistmaker.settings.domain


interface SettingsRepository {
    fun getThemeSettings(isDark: Boolean): Boolean
    fun updateThemeSettings(isDark: Boolean)
}