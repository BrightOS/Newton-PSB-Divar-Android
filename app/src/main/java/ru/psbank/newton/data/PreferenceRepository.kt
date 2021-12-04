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

    // E-Mail repository

    var email: String
        get() = sharedPreferences.getString(PREFERENCE_EMAIL, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_EMAIL, value).apply()

    private val _emailLive: MutableLiveData<String> = MutableLiveData()
    val emailLive: LiveData<String>
        get() = _emailLive

    // Phone repository

    var phone: String
        get() = sharedPreferences.getString(PREFERENCE_PHONE, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_PHONE, value).apply()

    private val _phoneLive: MutableLiveData<String> = MutableLiveData()
    val phoneLive: LiveData<String>
        get() = _phoneLive

    // Birth date repository

    var birthDate: String
        get() = sharedPreferences.getString(PREFERENCE_BIRTH_DATE, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_BIRTH_DATE, value).apply()

    private val _birthDateLive: MutableLiveData<String> = MutableLiveData()
    val birthDateLive: LiveData<String>
        get() = _birthDateLive

    // Reg date repository

    var regDate: String
        get() = sharedPreferences.getString(PREFERENCE_REG_DATE, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_REG_DATE, value).apply()

    private val _regDateLive: MutableLiveData<String> = MutableLiveData()
    val regDateLive: LiveData<String>
        get() = _regDateLive

    // User ID repository

    var userID: Int
        get() = sharedPreferences.getInt(PREFERENCE_ID, 0)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_ID, value).apply()

    private val _userIDLive: MutableLiveData<Int> = MutableLiveData()
    val userIDLive: LiveData<Int>
        get() = _userIDLive

    // Full name repository

    var fullName: String
        get() = sharedPreferences.getString(PREFERENCE_FULLNAME, "").toString()
        set(value) = sharedPreferences.edit().putString(PREFERENCE_FULLNAME, value).apply()

    private val _fullNameLive: MutableLiveData<String> = MutableLiveData()
    val fullNameLive: LiveData<String>
        get() = _fullNameLive

    // Project ID repository

    var projectID: Int?
        get() = sharedPreferences.getInt(PREFERENCE_PROJECT_ID, -1)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_PROJECT_ID, value!!).apply()

    private val _projectIDLive: MutableLiveData<Int?> = MutableLiveData()
    val projectIDLive: LiveData<Int?>
        get() = _projectIDLive

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

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCE_VIBRATION ->
                    _vibrationLive.value = vibration

                PREFERENCE_NIGHT_MODE ->
                    _nightModeLive.value = nightMode

                PREFERENCE_ANIMATIONS ->
                    _animationsLive.value = animations

                PREFERENCE_LOGIN ->
                    _loginLive.value = login

                PREFERENCE_PASSWORD ->
                    _loginLive.value = login

                PREFERENCE_TOKEN ->
                    _tokenLive.value = token
            }
        }

    init {
        // Init preference LiveData objects.
        _nightModeLive.value = nightMode

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    // Refresh main repository

    var refreshMain: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_REFRESH_MAIN, false)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_REFRESH_MAIN, value).apply()

    companion object {
        // Dark mode constants
        private const val PREFERENCE_NIGHT_MODE = "night_mode_psb"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        // User information constants
        private const val PREFERENCE_PHOTO = "photo_psb"
        private const val PREFERENCE_ID = "user_id_psb"
        private const val PREFERENCE_PROJECT_ID = "project_id_psb"
        private const val PREFERENCE_FULLNAME = "fullname_psb"
        private const val PREFERENCE_EMAIL = "email_psb"
        private const val PREFERENCE_BIRTH_DATE = "birth_date_psb"
        private const val PREFERENCE_REG_DATE = "reg_date_psb"
        private const val PREFERENCE_PHONE = "phone_psb"
        private const val PREFERENCE_LOGIN = "login_psb"
        private const val PREFERENCE_PASSWORD = "password_psb"
        private const val PREFERENCE_TOKEN = "token_psb"

        // Another constants
        const val PREFERENCE_VIBRATION = "vibration_psb"
        private const val PREFERENCE_ANIMATIONS = "animations_psb"
        private const val PREFERENCE_REFRESH_MAIN = "refresh_main_psb"
    }

}