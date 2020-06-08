package com.devhack.appdemofortests.di

import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        RegisterUserUseCase(get())
    }
}