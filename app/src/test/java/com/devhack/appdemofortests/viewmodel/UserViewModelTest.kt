package com.devhack.appdemofortests.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import co.devhack.base.Either
import co.devhack.base.State
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.ui.activities.viewmodels.UserViewModel
import com.devhack.appdemofortests.usecases.GetAllUsersUseCase
import com.devhack.appdemofortests.usecases.RegisterUserUseCase
import com.devhack.appdemofortests.usecases.User
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

    val testCoroutineDispatcher = TestCoroutineDispatcher()

    beforeGroup {
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

    group("Should ") {
        Feature("Register User") {

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
        }
    }

    afterGroup {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
})
