package com.devhack.appdemofortests.domain.usecase.user

import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
import com.devhack.appdemofortests.usecases.repository.UserRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
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
})
