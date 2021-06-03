package com.katyrin.searchtext.di

import com.katyrin.searchtext.App
import com.katyrin.searchtext.utils.FILE_NAME
import dagger.Module
import dagger.Provides
import java.io.BufferedReader
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun app(): App = app

    @Provides
    fun bufferReaderTextFile(app: App): BufferedReader = app.assets.open(FILE_NAME).bufferedReader()

    @Provides
    fun assetsText(bufferReaderTextFile: BufferedReader): String =
        bufferReaderTextFile.use { it.readText() }
}