package com.example.postask.presentation.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.postask.data.repository.StanManager
import com.example.postask.domain.repository.IsoRepository
import com.example.postask.domain.usecase.IsoInteractorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences("iso_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideIsoRepository(prefs: SharedPreferences): IsoRepository =
        StanManager(prefs)

    @Provides
    @Singleton
    fun provideIsoInteractor(repository: IsoRepository): IsoInteractorUseCase =
        IsoInteractorUseCase(repository)
}
