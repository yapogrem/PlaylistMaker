package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    companion object {
        fun getViewModelFactory(sharingInteractor: SharingInteractor,settingsInteractor: SettingsInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(sharingInteractor,settingsInteractor) as T
                }
            }
    }

    fun sharingApp() {
        sharingInteractor.shareApp()
    }

    fun messageToSupport() {
        sharingInteractor.openSupport()
    }

    fun openTermsUser() {
        sharingInteractor.openTerms()
    }

    fun changeTheme(isDark: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}