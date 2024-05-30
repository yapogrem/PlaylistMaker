package com.example.playlistmaker.settings.data.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return if (sharedPrefs.getBoolean(PLAYLIST_MAKER, false)) {
            ThemeSettings(
                isDark = true
            )
        } else {
            ThemeSettings(
                isDark = false
            )
        }
    }
}