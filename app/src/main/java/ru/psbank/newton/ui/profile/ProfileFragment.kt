package ru.psbank.newton.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.data.PreferenceRepository

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceRepository = (activity?.application as App).preferenceRepository

        if (preferenceRepository.animations) {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fio?.setText(preferenceRepository.fullName)
        email?.setText(preferenceRepository.email)
        phone?.setText(preferenceRepository.phone)
        birth_date?.setText(preferenceRepository.birthDate)
        registration_date?.setText(preferenceRepository.regDate)

        when (preferenceRepository.nightMode) {

            AppCompatDelegate.MODE_NIGHT_YES -> {
                dark_theme_check?.visibility = View.VISIBLE
                light_theme_check?.visibility = View.GONE
                auto_theme_check?.visibility = View.GONE
                system_theme_check?.visibility = View.GONE
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                dark_theme_check?.visibility = View.GONE
                light_theme_check?.visibility = View.VISIBLE
                auto_theme_check?.visibility = View.GONE
                system_theme_check?.visibility = View.GONE
            }

            AppCompatDelegate.MODE_NIGHT_AUTO -> {
                dark_theme_check?.visibility = View.GONE
                light_theme_check?.visibility = View.GONE
                auto_theme_check?.visibility = View.VISIBLE
                system_theme_check?.visibility = View.GONE
            }

            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                dark_theme_check?.visibility = View.GONE
                light_theme_check?.visibility = View.GONE
                auto_theme_check?.visibility = View.GONE
                system_theme_check?.visibility = View.VISIBLE
            }
        }

        dark_theme?.setOnClickListener {
            preferenceRepository.nightMode = AppCompatDelegate.MODE_NIGHT_YES
            dark_theme_check?.visibility = View.VISIBLE
            light_theme_check?.visibility = View.GONE
            auto_theme_check?.visibility = View.GONE
            system_theme_check?.visibility = View.GONE
        }

        light_theme?.setOnClickListener {
            preferenceRepository.nightMode = AppCompatDelegate.MODE_NIGHT_NO
            dark_theme_check?.visibility = View.GONE
            light_theme_check?.visibility = View.VISIBLE
            auto_theme_check?.visibility = View.GONE
            system_theme_check?.visibility = View.GONE
        }

        auto_theme?.setOnClickListener {
            preferenceRepository.nightMode = AppCompatDelegate.MODE_NIGHT_AUTO
            dark_theme_check?.visibility = View.GONE
            light_theme_check?.visibility = View.GONE
            auto_theme_check?.visibility = View.VISIBLE
            system_theme_check?.visibility = View.GONE
        }

        system_theme?.setOnClickListener {
            preferenceRepository.nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            dark_theme_check?.visibility = View.GONE
            light_theme_check?.visibility = View.GONE
            auto_theme_check?.visibility = View.GONE
            system_theme_check?.visibility = View.VISIBLE
        }
    }
}