package com.laba.uberreminder

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.preference.PreferenceManager


class FontSizeManager {
    fun adjustFontSize(context: Context) {
        /*val configuration: Configuration = context.getResources().getConfiguration()
        // This will apply to all text like -> Your given text size * fontScale
        configuration.fontScale = 10F
            /*PreferenceManager.getDefaultSharedPreferences(context).getString("font_size", "1")
                ?.toFloat() ?: 1.0F*/
        return context.createConfigurationContext(configuration)*/
        val configuration: Configuration = context.getResources().getConfiguration()
        configuration.fontScale =
        PreferenceManager.getDefaultSharedPreferences(context).getString("font_size", "1")
            ?.toFloat() ?: 1.0F
        val metrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        val wm =
            context.getSystemService(WINDOW_SERVICE) as WindowManager?
        wm!!.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        (context as MainActivity).getBaseContext().getResources().updateConfiguration(configuration, metrics)
    }
}
