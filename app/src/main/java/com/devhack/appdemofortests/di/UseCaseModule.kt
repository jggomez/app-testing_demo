package com.devhack.appdemofortests.di

import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import org.koin.dsl.module

val registerUserUseCaseModule = module {
    factory {
        RegisterUserUseCase(get())
    }
}

val getAllUserUseCaseModule = module {
    factory {
        GetAllUsersUseCase(get())
    }
}
