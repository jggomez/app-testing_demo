package com.devhack.appdemofortests.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import co.devhack.base.usecase.UseCase
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
import io.github.serpro69.kfaker.Faker
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
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
            val params = slot<RegisterUserUseCase.Params>()
            val state = slot<State>()
            val responseExpected = true
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)
            val user: User = mockk(relaxed = true)

            val response: Either<Failure, Boolean> = Either.Right(responseExpected)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)
            val observer: Observer<State> = spyk(Observer { })

            Given("Creating stubs and context") {
                userViewModel.registerUserLiveData.observeForever(observer)
                coEvery {
                    registerUserUseCase.run(capture(params))
                } returns response
                justRun {
                    observer.onChanged(capture(state))
                }
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.register(user)
                }
            }

            Then("Verify result success") {
                val respState = state.captured
                respState shouldBeInstanceOf State.Success::class.java
                when (respState) {
                    is State.Success -> {
                        respState.responseTo<Boolean>() shouldBeEqualTo true
                    }
                    else -> throw Exception("Incorrect State. This should be Success")
                }
            }

            Then("Verify dependencies ") {
                coVerify {
                    registerUserUseCase.run(params.captured)
                }
                confirmVerified()
            }
        }

        Scenario("Should not register and return error") {
            val params = slot<RegisterUserUseCase.Params>()
            val state = slot<State>()
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)
            val user: User = mockk(relaxed = true)

            val response: Either<Failure, Boolean> = Either.Left(Failure.NetworkConnection)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)
            val observer: Observer<State> = spyk(Observer { })

            Given("Creating stubs and context") {
                userViewModel.registerUserLiveData.observeForever(observer)
                coEvery {
                    registerUserUseCase.run(capture(params))
                } returns response
                justRun {
                    observer.onChanged(capture(state))
                }
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.register(user)
                }
            }

            Then("Verify result error") {
                val respState = state.captured
                respState shouldBeInstanceOf State.Failed::class.java
                when (respState) {
                    is State.Failed -> {
                        respState.failure shouldBeEqualTo Failure.NetworkConnection
                    }
                    else -> throw Exception("Incorrect State. This should be Failed")
                }
            }

            Then("Verify dependencies ") {
                coVerify {
                    registerUserUseCase.run(params.captured)
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
            val params = slot<UseCase.None>()
            val state = slot<State>()
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)

            val response: Either<Failure, List<User>> = Either.Right(UserViewModelTest.users)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)
            val observer: Observer<State> = spyk(Observer { })

            Given("Creating stubs and context") {
                userViewModel.getAllUsersLiveData.observeForever(observer)
                coEvery {
                    getAllUsersUseCase.run(capture(params))
                } returns response
                justRun {
                    observer.onChanged(capture(state))
                }
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.getAllUsers()
                }
            }

            Then("Verify dependencies") {
                val respState = state.captured
                respState shouldBeInstanceOf State.Success::class.java
                when (respState) {
                    is State.Success -> {
                        val data = respState.responseTo<List<User>>()
                        data.size shouldBeEqualTo UserViewModelTest.users.size
                    }
                    else -> throw Exception("Incorrect State")
                }
            }

            Then("Verify dependencies") {
                coVerify {
                    getAllUsersUseCase.run(params.captured)
                }
                confirmVerified(getAllUsersUseCase)
            }
        }

        Scenario("Should not get all users and return error") {
            val params = slot<UseCase.None>()
            val state = slot<State>()
            val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
            val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)

            val response: Either<Failure, List<User>> = Either.Left(Failure.NetworkConnection)
            val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)
            val observer: Observer<State> = spyk(Observer { })

            Given("Creating stubs and context") {
                userViewModel.getAllUsersLiveData.observeForever(observer)
                coEvery {
                    getAllUsersUseCase.run(capture(params))
                } returns response
                justRun {
                    observer.onChanged(capture(state))
                }
            }

            When("Calling to action") {
                testCoroutineDispatcher.runBlockingTest {
                    userViewModel.getAllUsers()
                }
            }

            Then("Verify result error") {
                val respState = state.captured
                respState shouldBeInstanceOf State.Failed::class.java
                when (respState) {
                    is State.Failed -> {
                        respState.failure shouldBeEqualTo Failure.NetworkConnection
                    }
                    else -> throw Exception("Incorrect State. This should be Failed")
                }
            }

            Then("Verify dependencies") {
                coVerify {
                    getAllUsersUseCase.run(params.captured)
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
