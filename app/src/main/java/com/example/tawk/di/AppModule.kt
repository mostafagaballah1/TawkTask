package com.example.tawk.di

import android.content.Context
import com.example.tawk.network.service.UsersServices
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.ui.fragments.users.UsersAdapter
import com.example.tawk.repos.UsersRepository
import com.example.tawk.room.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * App module
 * provides network related instances
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUsersRepository(
        userDao: UsersDao,
        services: UsersServices,
        cachePreferences: CachePreferences
    ) = UsersRepository( services, cachePreferences ,userDao)

    @Provides
    @Singleton
    fun providesGameAdapter(@ApplicationContext app: Context) = UsersAdapter(app)
}