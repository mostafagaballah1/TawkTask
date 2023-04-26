package com.example.tawk.di

import com.example.tawk.utils.MainView
import com.example.tawk.utils.MainViewImpl
import com.example.tawk.utils.NetworkConnectionUseCase
import com.example.tawk.utils.NetworkConnectionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {


    @Singleton
    @Binds
    abstract fun bindsNetworkConnectionUseCase(
        networkConnectionUseCaseImpl: NetworkConnectionUseCaseImpl
    ): NetworkConnectionUseCase


    @Singleton
    @Binds
    abstract fun bindsMainView(
        MainViewImpl: MainViewImpl
    ): MainView


}
