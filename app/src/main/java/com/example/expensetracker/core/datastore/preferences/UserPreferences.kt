package com.example.expensetracker.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.expensetracker.core.util.Constants
import com.example.expensetracker.core.util.Constants.SHOULD_SHOW_ONBOARDING
import com.example.expensetracker.core.util.Constants.THEME_OPTIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun setTheme(themeValue: String) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTIONS] = themeValue
        }
    }

    fun getTheme(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_OPTIONS] ?: Constants.LIGHT_MODE
        }
    }

    suspend fun setShouldShowOnboarding() {
        dataStore.edit { preferences ->
            preferences[SHOULD_SHOW_ONBOARDING] = false
        }
    }

    fun getShouldShowOnBoarding(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SHOULD_SHOW_ONBOARDING] ?: true
        }
    }

}