package ru.psbank.newton.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.psbank.newton.App
import ru.psbank.newton.data.PreferenceRepository
import java.util.concurrent.TimeUnit

/**
 * Этот класс необходим для того, чтобы
 * облегчить работу с вибрациями устройства.
 */
object VibrationUtil {
    fun vibratePhone(context: Context) {
        if (!context.getSharedPreferences(App.DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(PreferenceRepository.PREFERENCE_VIBRATION, false)
        ) return

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val vibrationEffect = VibrationEffect.createOneShot(
                    13, VibrationEffect.EFFECT_DOUBLE_CLICK
                )
                vibrator.vibrate(vibrationEffect)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val vibrationEffect = VibrationEffect.createOneShot(
                    13, VibrationEffect.DEFAULT_AMPLITUDE
                )
                vibrator.vibrate(vibrationEffect)
            }

            else -> vibrator.vibrate(TimeUnit.MILLISECONDS.toMillis(13))
        }
    }
}