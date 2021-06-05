package com.katyrin.searchtext.di

import com.katyrin.searchtext.repository.LocalRepository
import com.katyrin.searchtext.repository.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    @Singleton
    fun localRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository
}