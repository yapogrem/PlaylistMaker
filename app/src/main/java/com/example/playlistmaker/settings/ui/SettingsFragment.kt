package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val themeSwitcher = binding.themeSwitcher

        themeSwitcher.setChecked((requireContext().applicationContext as App).theme)

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.changeTheme(checked)
            val app = requireContext().applicationContext as App
            app.theme = checked
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
        return binding.root
    }
}