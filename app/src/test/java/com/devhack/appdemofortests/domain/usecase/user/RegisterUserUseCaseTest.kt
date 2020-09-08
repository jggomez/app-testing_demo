package com.devhack.appdemofortests.domain.usecase.user

import co.devhack.base.Either
import co.devhack.base.error.Failure
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
import com.devhack.appdemofortests.usecases.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object RegisterUserUseCaseTest : Spek({

    Feature("Register User") {

        beforeEachScenario { clearAllMocks() }

        Scenario("Should register or create user ") {
            val userRepository: UserRepository = mockk()
            val user: User = mockk(relaxed = true)
            val params = RegisterUserUseCase.Params(user)
            val response: Either<Failure, Boolean> = mockk(relaxed = true)
            val registerUserUseCase = RegisterUserUseCase(userRepository)
            var result: Either<Failure, Boolean>? = null

            Given("Creating Stubs") {
                coEvery { userRepository.add(user) } returns
                        response
            }

            When("Run to register user") {
                runBlocking {
                    result = registerUserUseCase.run(params)
                }
            }

            Then("Verify correct response") {
                result shouldBeEqualTo response
            }

            Then("Verify the called dependencies") {
                coVerify { userRepository.add(user) }
                confirmVerified(userRepository)
            }
        }
    }

    Feature("Get all users") {

        beforeEachScenario { clearAllMocks() }

        Scenario("Should get all users") {
            val userRepository: UserRepository = mockk()
            val response: Either<Failure, List<User>> = mockk(relaxed = true)
            val getAllUsersUseCase = GetAllUsersUseCase(userRepository)
            var result: Either<Failure, List<User>>? = null

            Given("Creating Stubs") {
                coEvery { userRepository.getAllUsers() } returns
                        response
            }

            When("Run to get all users") {
                runBlocking {
                    result = getAllUsersUseCase.run(UseCase.None())
                }
            }

            Then("Verify correct response") {
                result shouldBeEqualTo response
            }

            Then("Verify the called dependencies") {
                coVerify { userRepository.getAllUsers() }
                confirmVerified(userRepository)
            }
        }
    }
})
