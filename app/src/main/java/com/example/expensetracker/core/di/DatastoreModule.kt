package com.example.expensetracker.core.di
import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.expensetracker.core.datastore.preferences.UserPreferences
import com.example.expensetracker.core.util.Constants
import com.example.expensetracker.data.UserPreferenceRepositoryImpl
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
    @Provides
    @Singleton
    fun provideDatastorePreferences(@ApplicationContext context: Context):
            DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(name = Constants.USER_PREFERENCES)
            }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        userPreferences: UserPreferences
    ): UserPreferencesRepository {
        return UserPreferenceRepositoryImpl(
            preferences = userPreferences
        )
    }
}