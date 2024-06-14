package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.dto.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.impl.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchDiModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get())
    }
    single <SearchRepository>{
        SearchRepositoryImpl(get(), get())
    }
}