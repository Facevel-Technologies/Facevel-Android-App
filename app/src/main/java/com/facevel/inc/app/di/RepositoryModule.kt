package com.facevel.inc.app.di

import android.content.Context
import android.content.Intent
import com.facevel.inc.app.repository.AuthRepository
import com.facevel.inc.app.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalCoroutinesApi
object RepositoryModule {


    @Provides
    @Singleton
    fun providesUserRepository(
        @ApplicationContext context: Context
    ): UserRepository {
        return UserRepository(context)
    }


    @Provides
    @Singleton
    fun providesAuthRepository(
    ): AuthRepository {
        return AuthRepository()
    }
}