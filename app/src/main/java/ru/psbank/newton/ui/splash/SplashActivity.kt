package ru.psbank.newton.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.psbank.newton.App
import ru.psbank.newton.ui.MainActivity
import ru.psbank.newton.ui.auth.AuthActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceRepository = (application as App).preferenceRepository
        if (preferenceRepository.token != "")
            startActivity(Intent(this, MainActivity::class.java))
        else
            startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}