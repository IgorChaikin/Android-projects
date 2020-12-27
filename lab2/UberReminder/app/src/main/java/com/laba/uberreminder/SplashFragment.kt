package com.laba.uberreminder

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val isDark = preferences.getBoolean("dark", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        LocaleManager().setLocale(requireActivity())
        FontSizeManager().adjustFontSize(requireActivity())
        Handler(Looper.getMainLooper()).postDelayed({
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, SequencesListFragment())?.commit()
        }, 3000)
    }
}
