package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory(
                SharingInteractorImpl(),
                SettingsInteractorImpl()
            )
        )[SettingsViewModel::class.java]

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Кнопка назад
        binding.settingsBack.setOnClickListener {
            finish()
        }


//Переключение Темной темы
        val themeSwitcher = binding.themeSwitcher
//Переводим switch в сохраненное в SharedPreferences состояние
        themeSwitcher.setChecked((applicationContext as App).darkTheme)
//Переключаем тему по нажатию на switch
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }

        val displayShare = binding.settingsShare
        displayShare.setOnClickListener {
            viewModel.sharingApp()
        }

        val displaySupport = binding.settingsSupport
        displaySupport.setOnClickListener {
            viewModel.messageToSupport()
        }

        val displayUserAgreement = binding.settingsUserAgreement
        displayUserAgreement.setOnClickListener {
            viewModel.openTermsUser()
        }

    }
}



