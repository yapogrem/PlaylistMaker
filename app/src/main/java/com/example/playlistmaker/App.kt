package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor


const val PLAYLIST_MAKER = "playlist_maker_shared_preferences"
const val NIGHT_MODE_KEY = "night_mode_key"


class App : Application() {
    var theme : Boolean = false

    override fun onCreate() {
        super.onCreate()
        Creator.app = this
        val systemTheme = getSystemTheme()
        val settingsInteractor: SettingsInteractor = Creator.getSettingsIterator()
        theme = settingsInteractor.getThemeSettings(systemTheme)

        switchTheme(theme)
    }

    private fun switchTheme(theme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun getSystemTheme(): Boolean {
        val defaultState: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES
    }
}
