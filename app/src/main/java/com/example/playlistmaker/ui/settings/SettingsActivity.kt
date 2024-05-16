package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val displayMain = findViewById<ImageButton>(R.id.settings_back)
        displayMain.setOnClickListener {
            finish()
        }

//Переключение Темной темы
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
//Переводим switch в сохраненное в SharedPreferences состояние
        themeSwitcher.setChecked((applicationContext as App).darkTheme)
//Переключаем тему по нажатию на switch
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
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



