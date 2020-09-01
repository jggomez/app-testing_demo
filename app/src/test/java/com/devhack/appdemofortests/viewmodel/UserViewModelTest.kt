package com.devhack.appdemofortests.viewmodel

import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
object UserViewModelTest : Spek({

    val testCoroutineDispatcher = TestCoroutineDispatcher()
    val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    beforeGroup {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    group("Should ") {
        Feature("Register User") {

            Scenario("Should register the user correctly") {
                val state = slot<State>()
                val responseExpected = true
                val registerUserUseCase: RegisterUserUseCase = mockk(relaxed = true)
                val getAllUsersUseCase: GetAllUsersUseCase = mockk(relaxed = true)
                val userViewModel = UserViewModel(registerUserUseCase, getAllUsersUseCase)
                val user: User = mockk(relaxed = true)
                val params = RegisterUserUseCase.Params(user)
                val response: Either<Failure, Boolean> = Either.Right(responseExpected)
                val registerUserLiveData: MutableLiveData<State> = mockk(relaxed = true)

                Given("Creating stubs and context") {
                    coEvery {
                        registerUserUseCase.run(params)
                    } returns response

                    coEvery { registerUserLiveData.value = capture(state) } just runs
                }

                When("Calling to action") {
                    runBlockingTest {
                        userViewModel.register(user)
                    }
                }

                Then("Verify result success ") {
                    state.captured shouldBeEqualTo State.Success(responseExpected)
                }

                Then("Verify dependencies ") {
                    coVerify {
                        registerUserLiveData.value = state.captured
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
        testCoroutineScope.cleanupTestCoroutines()
    }
})
