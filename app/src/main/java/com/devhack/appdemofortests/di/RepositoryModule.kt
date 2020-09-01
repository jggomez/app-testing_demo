package com.devhack.appdemofortests.di

import com.devhack.appdemofortests.data.repositories.DataSource
import com.devhack.appdemofortests.data.repositories.FirestoreDataSource
import com.devhack.appdemofortests.data.repositories.UserRepositoryImp
import com.devhack.appdemofortests.usecases.repository.UserRepository
import org.koin.dsl.module

val userRepositoryModule = module {
    single<DataSource> {
        FirestoreDataSource()
    }

    factory<UserRepository> {
        UserRepositoryImp(get(), get())
    }
}
