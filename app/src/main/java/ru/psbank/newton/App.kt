package ru.psbank.newton

import android.app.Application
import android.content.ContentResolver
import androidx.appcompat.app.AppCompatDelegate
import ru.psbank.newton.data.PreferenceRepository

class App : Application() {
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        globalContentResolver = contentResolver

        super.onCreate()

        preferenceRepository = PreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, MODE_PRIVATE)
        )

        AppCompatDelegate.setDefaultNightMode(preferenceRepository.nightMode)
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences_psb"
        lateinit var globalContentResolver: ContentResolver
    }
}