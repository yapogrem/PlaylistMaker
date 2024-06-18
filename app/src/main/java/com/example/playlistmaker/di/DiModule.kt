package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.PLAYLIST_MAKER
import com.example.playlistmaker.search.data.network.ITunesApi
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val diModule = module {

    single<ITunesApi> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val iTunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ITunesApi::class.java)
    }

    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }
    factory { Gson() }

    includes(playerDiModule, searchDiModule, settingsDiModule)
}