package ru.psbank.newton.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    /**
     * Account information repositories
     */

    // Login repository

    var login: String
        get() = sharedPreferences.getString(PREFERENCE_LOGIN, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_LOGIN, value).apply()

    private val _loginLive: MutableLiveData<String> = MutableLiveData()
    val loginLive: LiveData<String>
        get() = _loginLive

    // Password repository

    var password: String
        get() = sharedPreferences.getString(PREFERENCE_PASSWORD, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_PASSWORD, value).apply()

    private val _passwordLive: MutableLiveData<String> = MutableLiveData()
    val passwordLive: LiveData<String>
        get() = _passwordLive

    // Token repository

    var token: String
        get() = sharedPreferences.getString(PREFERENCE_TOKEN, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_TOKEN, value).apply()

    private val _tokenLive: MutableLiveData<String> = MutableLiveData()
    val tokenLive: LiveData<String>
        get() = _tokenLive

    // Photo repository

    var photo: String
        get() = sharedPreferences.getString(PREFERENCE_PHOTO, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_PHOTO, value).apply()

    private val _photoLive: MutableLiveData<String> = MutableLiveData()
    val photoLive: LiveData<String>
        get() = _photoLive

    // User ID repository

    var userID: Int
        get() = sharedPreferences.getInt(PREFERENCE_ID, 0)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_ID, value).apply()

    private val _userIDLive: MutableLiveData<Int> = MutableLiveData()
    val userIDLive: LiveData<Int>
        get() = _userIDLive

    /**
     * Personalization repositories
     */

    // Vibration repository

    var vibration: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_VIBRATION, false)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_VIBRATION, value).apply()

    private val _vibrationLive: MutableLiveData<Boolean> = MutableLiveData()
    val vibrationLive: LiveData<Boolean>
        get() = _vibrationLive

    // Animations repository

    var animations: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_ANIMATIONS, true)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_ANIMATIONS, value).apply()

    private val _animationsLive: MutableLiveData<Boolean> = MutableLiveData()
    val animationsLive: LiveData<Boolean>
        get() = _animationsLive

    // Night mode repository

    var nightMode: Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, value).apply()
            AppCompatDelegate.setDefaultNightMode(value)
        }

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive

    companion object {
        // Dark mode constants
        private const val PREFERENCE_NIGHT_MODE = "night_mode_psb"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        // User information constants
        private const val PREFERENCE_PHOTO = "photo_psb"
        private const val PREFERENCE_ID = "id_psb"
        private const val PREFERENCE_LOGIN = "login_psb"
        private const val PREFERENCE_PASSWORD = "password_psb"
        private const val PREFERENCE_TOKEN = "token_psb"

        // Another constants
        const val PREFERENCE_VIBRATION = "vibration_psb"
        private const val PREFERENCE_ANIMATIONS = "animations_psb"
    }

}