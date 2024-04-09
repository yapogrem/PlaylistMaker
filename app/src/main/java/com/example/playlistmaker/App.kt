package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import kotlin.properties.Delegates


const val PLAYLIST_MAKER = "playlist_maker_shared_preferences"
const val NIGHT_MODE_KEY = "night_mode_key"


class App : Application() {
    var darkTheme : Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        val systemTheme = isDarkThemeEnabled()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(NIGHT_MODE_KEY,systemTheme)
        darkTheme = isDarkThemeEnabled()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        //Сохраняем состояние в sharedPreferences
        sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, darkThemeEnabled).apply()
        //Переключаем тему
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun isDarkThemeEnabled(): Boolean {
        val defaultState: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultState == Configuration.UI_MODE_NIGHT_YES
    }
}
