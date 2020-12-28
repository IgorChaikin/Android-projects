package com.laba.uberreminder

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import java.util.*

class LocaleManager {
    fun setLocale(activity: Activity) {
        val languageCode = PreferenceManager.getDefaultSharedPreferences(activity).getString("language", "en")
        val locale = Locale(languageCode ?: "en")
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
