package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate




class App : Application() {
    companion object{
        const val PLAYLISTMAKER = "playlist_maker_shared_preferences"
        const val NIGHT_MODE_KEY = "night_mode_key"
    }
    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLISTMAKER, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(NIGHT_MODE_KEY, false)
        if (darkTheme) {
            switchTheme(true)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
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
}