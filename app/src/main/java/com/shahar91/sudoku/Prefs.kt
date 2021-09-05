package com.shahar91.sudoku

import android.content.Context
import android.preference.PreferenceManager

import android.os.Bundle

import android.preference.PreferenceActivity


class Prefs : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    companion object {
        private const val OPT_MUSIC = "music"
        private const val OPT_MUSIC_DEF = true
        private const val OPT_HINTS = "hints"
        private const val OPT_HINTS_DEF = true
        fun getMusic(context: Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_MUSIC, OPT_MUSIC_DEF)
        }

        fun getHints(context: Context?): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_HINTS, OPT_HINTS_DEF)
        }
    }
}
