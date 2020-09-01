package com.devhack.appdemofortests.di

import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userViewModelModule = module {
    viewModel {
        UserViewModel(get(), get())
    }
}
