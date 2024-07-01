package com.example.playlistmaker.di

import com.example.playlistmaker.mediathek.ui.FragmentFavoritesTracks
import com.example.playlistmaker.mediathek.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val favoritesTracksDiModule = module {
    factory<FragmentFavoritesTracks> {
        FragmentFavoritesTracks()
    }
    viewModel {
        PlaylistsViewModel()
    }
}