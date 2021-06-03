package com.katyrin.searchtext

import android.app.Application
import com.katyrin.searchtext.di.AppComponent
import com.katyrin.searchtext.di.AppModule
import com.katyrin.searchtext.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object{
        lateinit var appInstance: App
    }
}