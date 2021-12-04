package ru.psbank.newton.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.psbank.newton.App
import ru.psbank.newton.ui.MainActivity
import ru.psbank.newton.ui.auth.AuthActivity
import ru.psbank.newton.util.api.Auth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceRepository = (application as App).preferenceRepository

        if (preferenceRepository.token != "")
            GlobalScope.launch {
                val response = Auth(
                    preferenceRepository.login,
                    preferenceRepository.password
                ).post()

                when (response.code) {
                    200 -> {
                        val responseObject = JSONObject(response.body)
                        val participantObject = responseObject.getJSONObject("participant")
                        val tokenObject = responseObject.getJSONObject("token")

                        preferenceRepository.token = tokenObject.getString("value")
                        preferenceRepository.fullName =
                            "${participantObject.getString("second_name")} ${
                                participantObject.getString("first_name")
                            } ${participantObject.getString("third_name")}"
                        preferenceRepository.userID = participantObject.getInt("id")
                        preferenceRepository.login = participantObject.getString("login")
                        preferenceRepository.email = participantObject.getString("email")
                        preferenceRepository.birthDate = participantObject.getString("dt_birth")
                        preferenceRepository.regDate = participantObject.getString("created")
                        preferenceRepository.phone = participantObject.getString("phone")
                        preferenceRepository.password = participantObject.getString("password")
                        preferenceRepository.projectID =
                            participantObject.getString("project_id").let {
                                if (it == "null")
                                    -1
                                else
                                    Integer.parseInt(it)
                            }

                        MainScope().launch {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                    else -> {
                        MainScope().launch {
                            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                            finish()
                        }
                    }
                }

            }
        else {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }
    }
}