package com.devhack.appdemofortests.usecases

import co.devhack.base.Either
import co.devhack.base.error.Failure
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.usecases.repository.UserRepository

class RegisterUserUseCase(
    private val userRepository: UserRepository
) : UseCase<Boolean, RegisterUserUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, Boolean> =
        userRepository.add(params.user)

    data class Params(val user: User)
}
