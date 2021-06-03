package com.katyrin.searchtext.di

import com.katyrin.searchtext.ui.MainFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, PresentationModule::class])
@Singleton
interface AppComponent {
    fun inject(mainFragment: MainFragment)
}