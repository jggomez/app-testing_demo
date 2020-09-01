package com.devhack.appdemofortests.di

import co.devhack.androidextensions.network.NetworkHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkHandlerModule = module {
    single {
        NetworkHandler(androidContext())
    }
}
