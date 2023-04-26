package com.example.tawk.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.persistance.CachePreferencesImpl
import com.example.tawk.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    const val APP_DATABASE_NAME = "users_db"

    private val Context.userPreferencesDataStore by preferencesDataStore(
        name = "user"
    )

    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.usersDao()

    @Singleton
    @Provides
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.userPreferencesDataStore

    @Provides
    @Singleton
    fun providesCachePreference(dataStore: DataStore<Preferences>): CachePreferences =
        CachePreferencesImpl(dataStore)

}