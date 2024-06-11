package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl():

    SharingInteractor {
    private val externalNavigator: ExternalNavigator = Creator.getExternalNavigator()
    override fun shareApp(){
        externalNavigator.shareLink()
    }
    override fun openTerms(){
        externalNavigator.openLink()
    }
    override fun openSupport(){
        externalNavigator.openEmail()
    }
}