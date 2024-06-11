package com.example.playlistmaker.sharing.data.impl

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator

class ExternalNavigatorImpl(private val app: Application) : ExternalNavigator {
    override fun shareLink() {
        val message = app.getString(R.string.settings_share_message)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
        }
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        app.baseContext.startActivity(intent)
    }

    override fun openLink() {
        val offerLink = app.getString(R.string.settings_agreement_url)
        val url = Uri.parse(offerLink)
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        app.baseContext.startActivity(intent)
    }

    override fun openEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val message = app.getString(R.string.settings_support_message)
            val subject = app.getString(R.string.settings_support_subject)
            val mail = app.getString(R.string.settings_support_mail)
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(intent)
    }
}