package com.example.expensetracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun setTheme(themeValue:String)

    fun getTheme(): Flow<String>

    suspend fun setShouldShowOnboarding()

    fun getShouldShowOnBoarding(): Flow<Boolean>
}