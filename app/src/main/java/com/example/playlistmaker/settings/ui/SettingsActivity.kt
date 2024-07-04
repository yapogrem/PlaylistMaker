package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Кнопка назад
        binding.settingsBack.setOnClickListener {
            finish()
        }

//Переключение Темной темы
        val themeSwitcher = binding.themeSwitcher
//Переводим switch в сохраненное в SharedPreferences состояние
        themeSwitcher.setChecked((applicationContext as App).theme)
//Переключаем тему по нажатию на switch
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.changeTheme(checked)
            val app=applicationContext as App
            app.theme=checked
        }

        val displayShare = binding.settingsShare
        displayShare.setOnClickListener {
            settingsViewModel.sharingApp()
        }

        val displaySupport = binding.settingsSupport
        displaySupport.setOnClickListener {
            settingsViewModel.messageToSupport()
        }

        val displayUserAgreement = binding.settingsUserAgreement
        displayUserAgreement.setOnClickListener {
            settingsViewModel.openTermsUser()
        }
    }
}



