package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

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
        settingsInteractor.updateThemeSettings(isDark)
    }


//    companion object {
//        fun getViewModelFactory(
//            sharingInteractor: SharingInteractor,
//            settingsInteractor: SettingsInteractor
//        ): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return SettingsViewModel(sharingInteractor, settingsInteractor) as T
//                }
//            }
//    }


}