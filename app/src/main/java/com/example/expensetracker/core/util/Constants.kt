package com.example.expensetracker.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val DATABASE_NAME="expensetracker.db"

    const val USER_PREFERENCES = "USER_PREFERENCES"

    val THEME_OPTIONS = stringPreferencesKey(name = "theme_option")
    val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey(name = "should_show_onboarding")

    const val LIGHT_MODE = "LIGHT_MODE"
    const val DARK_MODE = "DARK_MODE"
    const val SYSTEM_DEFAULT = "SYSTEM_DEFAULT"
}