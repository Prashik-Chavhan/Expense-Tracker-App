package com.example.expensetracker.data

import com.example.expensetracker.core.datastore.preferences.UserPreferences
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(
    private val preferences: UserPreferences
): UserPreferencesRepository {

    override suspend fun setTheme(themeValue: String) {
        preferences.setTheme(themeValue = themeValue)
    }

    override fun getTheme(): Flow<String> {
        return preferences.getTheme()
    }

    override suspend fun setShouldShowOnboarding(){
        preferences.setShouldShowOnboarding()
    }

    override fun getShouldShowOnBoarding(): Flow<Boolean> {
        return preferences.getShouldShowOnBoarding()
    }
}