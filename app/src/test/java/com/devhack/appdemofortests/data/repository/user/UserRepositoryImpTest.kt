package com.devhack.appdemofortests.data.repository.user

import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.data.repositories.DataSource
import com.devhack.appdemofortests.data.repositories.UserEntity
import com.devhack.appdemofortests.data.repositories.UserRepositoryImp
import com.devhack.appdemofortests.usecases.User
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object UserRepositoryImpTest : Spek({

    Feature("Add User") {

        val netWorkHandler: NetworkHandler = mockk()
        val dataSource: DataSource = mockk()
        val userRepository =
            UserRepositoryImp(
                netWorkHandler,
                dataSource
            )

        beforeEachScenario { clearAllMocks() }

        Scenario("Should add user") {

            val slotUserEntity = slot<UserEntity>()
            val user: User = mockk(relaxed = true)
            lateinit var result: Either<Failure, Boolean>

            Given("Network is connected and creating stubs") {
                every { netWorkHandler.isConnected } returns
                        UserRepositoryImpTest.NETWORK_CONNECTED
                coEvery { dataSource.add(capture(slotUserEntity)) } returns
                        UserRepositoryImpTest.SUCCESSFUL_OPERATION
            }

            When("Run to add user") {
                runBlocking {
                    result = userRepository.add(user)
                }
            }

            Then("Verify result success") {
                (result as Either.Right).b.shouldBeTrue()
            }

            Then("Verify the called to dependencies") {
                coVerifySequence {
                    netWorkHandler.isConnected
                    dataSource.add(slotUserEntity.captured)
                }
            }
        }

        Scenario("Should not create user because there is not connection") {

            val user: User = mockk(relaxed = true)
            lateinit var result: Either<Failure, Boolean>

            Given("Network is connected") {
                every { netWorkHandler.isConnected } returns
                        UserRepositoryImpTest.NETWORK_DISCONNECTED
            }

            When("Run to add user") {
                runBlocking {
                    result = userRepository.add(user)
                }
            }

            Then("Verify result success") {
                (result as Either.Left).a shouldBeEqualTo Failure.NetworkConnection
            }

            Then("Verify the called to dependencies") {
                verify(exactly = UserRepositoryImpTest.VERIFY_ONE_INTERACTION)
                { netWorkHandler.isConnected }
                coVerify { dataSource.add(any()) wasNot Called }
                confirmVerified(netWorkHandler, dataSource)
            }
        }
    }

}) {

    private const val NETWORK_CONNECTED = true
    private const val NETWORK_DISCONNECTED = false
    private const val SUCCESSFUL_OPERATION = true
    private const val VERIFY_ONE_INTERACTION = 1
}