package com.example.playlistmaker.settings.domain


interface  SettingsInteractor {
    fun getThemeSettings(isDark: Boolean): Boolean

    fun updateThemeSettings(isDark: Boolean)
}