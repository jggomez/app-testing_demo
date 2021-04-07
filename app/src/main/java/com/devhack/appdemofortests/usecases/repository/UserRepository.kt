package com.devhack.appdemofortests.usecases.repository

import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.usecases.User

interface UserRepository {
    suspend fun add(user: User): Either<Failure, Boolean>
    suspend fun getAllUsers(): Either<Failure, List<User>>
}
