package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.diModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin


const val PLAYLIST_MAKER = "playlist_maker_shared_preferences"
const val NIGHT_MODE_KEY = "night_mode_key"


class App : Application() {
    var theme : Boolean = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(diModule)
        }

        val systemTheme = getSystemTheme()
        val settingsInteractor: SettingsInteractor = getKoin().get()
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
