package com.devhack.appdemofortests.data.repositories

import co.devhack.androidextensions.exception.toCustomExceptions
import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.usecases.User
import com.devhack.appdemofortests.usecases.repository.UserRepository

class UserRepositoryImp(
    private val networkHandler: NetworkHandler,
    private val dataSource: DataSource
) : UserRepository {

    override suspend fun add(user: User): Either<Failure, Boolean> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.add(
                        UserEntity(
                            user.name,
                            user.lastName,
                            user.cellPhone,
                            user.address
                        )
                    )
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (ex: java.lang.Exception) {
            Either.Left(ex.toCustomExceptions())
        }

    override suspend fun getAllUsers(): Either<Failure, List<User>> =
        try {
            when (networkHandler.isConnected) {
                true -> Either.Right(
                    dataSource.getAllUsers().map {
                        it.toUser()
                    }
                )
                else -> Either.Left(Failure.NetworkConnection)
            }
        } catch (ex: java.lang.Exception) {
            Either.Left(ex.toCustomExceptions())
        }
}
