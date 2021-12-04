package ru.psbank.newton.ui.auth

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.widget.doAfterTextChanged
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.ui.MainActivity
import ru.psbank.newton.util.api.Auth

class AuthActivity : AppCompatActivity() {

    private lateinit var stringWithProgress: SpannableString

    override fun onCreate(savedInstanceState: Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        login?.editText?.doAfterTextChanged {
            login?.isErrorEnabled = false
            password?.isErrorEnabled = false
        }

        password?.editText?.doAfterTextChanged {
            login?.isErrorEnabled = false
            password?.isErrorEnabled = false
        }

        initializeStringWithProgress()

        auth?.setOnClickListener {
            val isLoginEmpty = login?.editText?.text.toString().trim().length == 0
            val isPasswordEmpty = password?.editText?.text.toString().trim().length == 0

            if (isLoginEmpty)
                login?.error = "Поле логина не может быть пустым."

            if (isPasswordEmpty)
                password?.error = "Поле с паролем не может быть пустым."

            if (isPasswordEmpty || isLoginEmpty)
                return@setOnClickListener

            auth?.text = stringWithProgress

            GlobalScope.launch {
                val response = Auth(
                    login?.editText?.text.toString(),
                    password?.editText?.text.toString()
                ).post()

                println("${login?.editText?.text.toString()} ${password?.editText?.text.toString()}")

                when (response.code) {
                    400 -> {
                        MainScope().launch {
                            login?.error = " "
                            password?.error = "Неверный логин или пароль."
                            auth?.text = "Войти"
                        }
                    }
                    200 -> {
                        val responseObject = JSONObject(response.body)
                        val participantObject = responseObject.getJSONObject("participant")
                        val tokenObject = responseObject.getJSONObject("token")

                        val preferenceRepository = (application as App).preferenceRepository
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

                        startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                        finish()
                    }
                    500 -> {
                        MainScope().launch {
                            login?.error = " "
                            password?.error = "Произошла ошибка со стороны сервера"
                            auth?.text = "Войти"
                        }
                    }
                    else -> {
                        MainScope().launch {
                            login?.error = " "
                            password?.error = "Произошла неизвестная ошибка."
                            auth?.text = "Войти"
                        }
                    }
                }
            }
        }
    }

    private fun initializeStringWithProgress() {
        val progressDrawable = CircularProgressDrawable(this).apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(Color.WHITE)

            val size = (centerRadius + strokeWidth).toInt() * 2
            setBounds(0, 0, size, size)
        }


        val drawableSpan = CustomDrawableSpan(progressDrawable, paddingStart = 30)

        stringWithProgress = SpannableString("Загрузка ").apply {
            setSpan(drawableSpan, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        progressDrawable.start()

        val callback = object : Drawable.Callback {
            override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            }

            override fun invalidateDrawable(who: Drawable) {
                auth?.invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            }
        }
        progressDrawable.callback = callback
    }

    inner class CustomDrawableSpan(
        drawable: Drawable,
        var paddingStart: Int = 0,
        var paddingEnd: Int = 0
    ) : ImageSpan(drawable) {
        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fontMetricsInt: Paint.FontMetricsInt?
        ): Int {
            val rect = drawable.bounds
            fontMetricsInt?.let {
                val fontMetrics = paint.fontMetricsInt

                val lineHeight = fontMetrics.bottom - fontMetrics.top

                val drHeight = Math.max(lineHeight, rect.bottom - rect.top)

                val centerY = fontMetrics.top + lineHeight / 2

                fontMetricsInt.apply {
                    ascent = centerY - drHeight / 2
                    descent = centerY + drHeight / 2
                    top = ascent
                    bottom = descent
                }
            }

            return rect.width() + paddingStart
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {

            canvas.save()
            val fontMetrics = paint.fontMetricsInt
            val lineHeight = fontMetrics.bottom - fontMetrics.top

            val centerY = y + fontMetrics.bottom - lineHeight / 2
            val transY = centerY - drawable.bounds.height() / 2

            canvas.translate(x + paddingStart, transY.toFloat())

            drawable.draw(canvas)

            canvas.restore()
        }
    }
}