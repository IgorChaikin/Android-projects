package com.laba.uberreminder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat() {
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener(){ p0, p1 ->
        if(p1 == "font_size"){
            FontSizeManager().adjustFontSize(requireActivity())
        } else if(p1 == "language"){
            LocaleManager().setLocale(requireActivity())
        } else if(p1 == "dark"){
            val isDark = p0.getBoolean("dark", false)
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if(preference?.key == "clear_data"){
            SequencesRepository(requireContext()).clear()
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onDetach() {
        super.onDetach()
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)
    }
}
