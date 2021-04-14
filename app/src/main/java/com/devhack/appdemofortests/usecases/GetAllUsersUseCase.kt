package com.devhack.appdemofortests.usecases

import co.devhack.base.Either
import co.devhack.base.error.*
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.usecases.repository.UserRepository

class GetAllUsersUseCase(
    private val userRepository: UserRepository
) : UseCase<List<User>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, List<User>> = userRepository.getAllUsers()
}
