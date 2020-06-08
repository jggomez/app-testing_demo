package com.devhack.appdemofortests.ui.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User

class UserViewModel(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    val registerUserLiveData by lazy {
        MutableLiveData<State>()
    }

    fun register(user: User) {
        registerUserLiveData.value = State.Loading
        registerUserUseCase.invoke(
            viewModelScope,
            RegisterUserUseCase.Params(user)
        ) {
            it.either(::handleError, ::successRegisterUser)
        }
    }

    private fun handleError(failure: Failure) {
        registerUserLiveData.value = State.Failed(failure)
    }

    private fun successRegisterUser(success: Boolean) {
        registerUserLiveData.value = State.Success(success)
    }

}