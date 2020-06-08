package com.devhack.appdemofortests

import android.app.Application
import com.devhack.appdemofortests.di.userRepositoryModule
import com.devhack.appdemofortests.di.userUseCaseModule
import com.devhack.appdemofortests.di.userViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ApplicationBase : Application() {

    override fun onCreate() {
        super.onCreate()
        registerKoinModules()
    }

    private fun registerKoinModules() {
        startKoin {
            androidLogger()
            androidContext(this@ApplicationBase)
            modules(
                listOf(
                    userRepositoryModule,
                    userUseCaseModule,
                    userViewModelModule
                )
            )
        }
    }
}