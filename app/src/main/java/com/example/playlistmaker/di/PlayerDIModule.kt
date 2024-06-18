package com.example.playlistmaker.di


import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerInteractor
import com.example.playlistmaker.player.data.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

val playerDiModule = module {
    factory <MediaPlayer> {
        MediaPlayer()
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    viewModel {
        PlayerViewModel(get())
    }
}