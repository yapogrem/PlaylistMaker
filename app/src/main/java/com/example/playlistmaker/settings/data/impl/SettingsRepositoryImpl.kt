package com.example.playlistmaker.settings.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.NIGHT_MODE_KEY
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)
    override fun getThemeSettings(systemTheme: Boolean):Boolean {
        return sharedPrefs.getBoolean(NIGHT_MODE_KEY, systemTheme)
    }

    @SuppressLint("CommitPrefEdits")
    override fun updateThemeSettings(isDark: Boolean) {
        sharedPrefs.edit().putBoolean(NIGHT_MODE_KEY, isDark).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })
    }
}