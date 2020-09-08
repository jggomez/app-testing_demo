package com.devhack.appdemofortests.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
import io.github.serpro69.kfaker.Faker
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature


@ExperimentalCoroutinesApi
object UserViewModelTest : Spek({


    Feature("Register User") {

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        beforeEachScenario {
            Dispatchers.setMain(testCoroutineDispatcher)
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }
            })
        }

        Scenario("Should register the user correctly") {
            val state = slot<State>()
            val responseExpected = true
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)
            val user: User = mockk(relaxed = true)
            val params = RegisterUserUseCase.Params(user)

            val response: Either<Failure, Boolean> = Either.Right(responseExpected)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)

            Given("Creating stubs and context") {
                coEvery {
                    registerUserUseCase.run(params)
                } returns response
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.register(user)
                }
            }

            Then("Verify dependencies ") {
                coVerify {
                    registerUserUseCase.run(params)
                }
                confirmVerified()
            }
        }

        afterEachScenario {
            Dispatchers.resetMain()
            testCoroutineDispatcher.cleanupTestCoroutines()
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }

    Feature("Get All Users") {

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        beforeEachScenario {
            Dispatchers.setMain(testCoroutineDispatcher)
            ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean {
                    return true
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }
            })
        }

        Scenario("Should get all users correctly") {
            val state = slot<State>()
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)

            val response: Either<Failure, List<User>> = Either.Right(UserViewModelTest.users)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)

            Given("Creating stubs and context") {
                coEvery {
                    getAllUsersUseCase.run(UseCase.None())
                } returns response
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.getAllUsers()
                }
            }

            Then("Verify dependencies") {
                coVerify {
                    getAllUsersUseCase.run(UseCase.None())
                }
                confirmVerified(getAllUsersUseCase)
            }
        }

        afterEachScenario {
            Dispatchers.resetMain()
            testCoroutineDispatcher.cleanupTestCoroutines()
            ArchTaskExecutor.getInstance().setDelegate(null)
        }
    }
}) {

    private val users: List<User>
        get() = mutableListOf<User>().apply {
            add(createUser())
            add(createUser())
            add(createUser())
            add(createUser())
            add(createUser())
        }

    private fun createUser(): User {
        val faker = Faker()
        return User(
            faker.name.firstName(),
            faker.name.lastName(),
            faker.phoneNumber.phoneNumber(),
            faker.address.fullAddress()
        )
    }
}
