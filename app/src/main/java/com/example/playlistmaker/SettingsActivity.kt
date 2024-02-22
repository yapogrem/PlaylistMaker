package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val displayMain = findViewById<ImageButton>(R.id.settings_back)
        displayMain.setOnClickListener {
            finish()
        }

        val displayShare= findViewById<ImageButton>(R.id.settingsShare)
        displayShare.setOnClickListener {
            val message = getString(R.string.settings_share_message)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent,"Share"))
        }

        val displaySupport= findViewById<ImageButton>(R.id.settingsSupport)
        displaySupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                val message = getString(R.string.settings_support_message)
                val subject = getString(R.string.settings_support_subject)
                val mail = getString(R.string.settings_support_mail)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }
        }

        val displayUserAgreement = findViewById<ImageButton>(R.id.settingsUserAgreement)
        displayUserAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.settings_agreement_url))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}