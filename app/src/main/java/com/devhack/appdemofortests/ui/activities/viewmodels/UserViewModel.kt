package com.devhack.appdemofortests.ui.activities.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.devhack.base.State
import co.devhack.base.error.Failure
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
import kotlinx.coroutines.launch

class UserViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    private val _registerUserLiveData = MutableLiveData<State>()

    val registerUserLiveData: LiveData<State>
        get() = _registerUserLiveData


    val getAllUsersLiveData by lazy {
        MutableLiveData<State>()
    }

    fun register(user: User) {
        _registerUserLiveData.value = State.Loading
        viewModelScope.launch {
            registerUserUseCase.run(RegisterUserUseCase.Params(user))
                .either(::handleErrorRegisterUser, ::successRegisterUser)
        }
    }

    fun getAllUsers() {
        getAllUsersLiveData.value = State.Loading
        viewModelScope.launch {
            getAllUsersUseCase.run(UseCase.None())
                .either(::handleErrorGetAllUsers, ::successGetAllUsers)
        }
    }

    private fun handleErrorRegisterUser(failure: Failure) {
        _registerUserLiveData.value = State.Failed(failure)
    }

    private fun successRegisterUser(success: Boolean) {
        _registerUserLiveData.value = State.Success(success)
    }

    private fun handleErrorGetAllUsers(failure: Failure) {
        getAllUsersLiveData.value = State.Failed(failure)
    }

    private fun successGetAllUsers(users: List<User>) {
        getAllUsersLiveData.value = State.Success(users)
    }
}