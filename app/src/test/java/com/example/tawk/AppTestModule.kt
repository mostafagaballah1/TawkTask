package com.example.tawk

import android.content.Context
import com.example.tawk.network.service.UsersServices
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.repos.UsersRepository
import com.example.tawk.room.UsersDao
import com.example.tawk.ui.fragments.users.UsersAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppTestModule {

    @Provides
    @Singleton
    fun providesJobsRepository(
        jobDao: UsersDao,
        services: UsersServices,
        cachePreferences: CachePreferences
    ) = UsersRepository( services, cachePreferences ,jobDao)

    @Provides
    @Singleton
    fun providesGameAdapter(@ApplicationContext app: Context) = UsersAdapter(app)
}