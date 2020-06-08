package com.devhack.appdemofortests.data.repositories

import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.usecases.User
import com.devhack.appdemofortests.usecases.repository.UserRepository

class UserRepositoryImp(
    private val dataSource: DataSource
) : UserRepository {

    override suspend fun add(user: User): Either<Failure, Boolean> =
        try {
            Either.Right(
                dataSource.add(
                    UserEntity(
                        user.name,
                        user.lastName,
                        user.cellPhone,
                        user.address
                    )
                )
            )
        } catch (e: Exception) {
            Either.Left(Failure.GenericError(e))
        }

}