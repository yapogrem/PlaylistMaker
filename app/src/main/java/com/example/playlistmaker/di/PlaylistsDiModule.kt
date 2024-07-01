package com.example.playlistmaker.di

import com.example.playlistmaker.mediathek.ui.FragmentPlaylists
import com.example.playlistmaker.mediathek.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsDiModule = module {
    factory <FragmentPlaylists> {
        FragmentPlaylists()
    }
    viewModel {
        PlaylistsViewModel()
    }
}