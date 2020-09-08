package com.devhack.appdemofortests.data.repository.user

import co.devhack.androidextensions.network.NetworkHandler
import co.devhack.base.Either
import co.devhack.base.error.Failure
import com.devhack.appdemofortests.data.repositories.DataSource
import com.devhack.appdemofortests.data.repositories.UserEntity
import com.devhack.appdemofortests.data.repositories.UserRepositoryImp
import com.devhack.appdemofortests.usecases.User
import io.github.serpro69.kfaker.Faker
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object UserRepositoryImpTest : Spek({

    Feature("Add User") {

        val networkHandler: NetworkHandler = mockk()
        val dataSource: DataSource = mockk()
        val userRepository =
            UserRepositoryImp(
                networkHandler,
                dataSource
            )

        beforeEachScenario { clearAllMocks() }

        Scenario("Should add user") {

            val slotUserEntity = slot<UserEntity>()
            val user: User = mockk(relaxed = true)
            lateinit var result: Either<Failure, Boolean>

            Given("Network is connected and creating stubs") {
                every { networkHandler.isConnected } returns
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
                    networkHandler.isConnected
                    dataSource.add(slotUserEntity.captured)
                }
            }
        }

        Scenario("Should not create user because there is not connection") {

            val user: User = mockk(relaxed = true)
            lateinit var result: Either<Failure, Boolean>

            Given("Network is connected") {
                every { networkHandler.isConnected } returns
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
                { networkHandler.isConnected }
                coVerify { dataSource.add(any()) wasNot Called }
                confirmVerified(networkHandler, dataSource)
            }
        }

        Scenario("Should generate a Generic Error") {
            val user: User = mockk(relaxed = true)
            val error = UserRepositoryImpTest.ERROR_GENERIC
            lateinit var result: Either<Failure, Boolean>

            Given("Network is connected and data source return an Error") {
                every { networkHandler.isConnected } returns UserRepositoryImpTest.NETWORK_CONNECTED
                coEvery {
                    dataSource.add(ofType())
                } throws Exception(error)
            }

            When("Run to add user") {
                runBlocking {
                    result = userRepository.add(user)
                }
            }

            Then("Verify called dependencies handler") {
                coVerifySequence {
                    networkHandler.isConnected
                    dataSource.add(ofType())
                }
            }

            Then("Verify Exception") {
                ((result as Either.Left).a
                        as Failure.GenericError)
                    .exception.message shouldBeEqualTo error
            }
        }

    }

    Feature("Get All User") {

        val networkHandler: NetworkHandler = mockk()
        val dataSource: DataSource = mockk()
        val userRepository =
            UserRepositoryImp(
                networkHandler,
                dataSource
            )

        beforeEachScenario { clearAllMocks() }

        Scenario("Should get all user") {

            lateinit var result: Either<Failure, List<User>>

            Given("Network is connected and creating stubs") {
                every { networkHandler.isConnected } returns
                        UserRepositoryImpTest.NETWORK_CONNECTED
                coEvery { dataSource.getAllUsers() } returns
                        UserRepositoryImpTest.users
            }

            When("Run to add user") {
                runBlocking {
                    result = userRepository.getAllUsers()
                }
            }

            Then("Verify result success") {
                (result as Either.Right).b.size shouldBeGreaterThan 0
            }

            Then("Verify the called to dependencies") {
                coVerifySequence {
                    networkHandler.isConnected
                    dataSource.getAllUsers()
                }
            }
        }

        Scenario("Should not get all user because there is not connection") {

            lateinit var result: Either<Failure, List<User>>

            Given("Network is connected") {
                every { networkHandler.isConnected } returns
                        UserRepositoryImpTest.NETWORK_DISCONNECTED
            }

            When("Run to add user") {
                runBlocking {
                    result = userRepository.getAllUsers()
                }
            }

            Then("Verify result success") {
                (result as Either.Left).a shouldBeEqualTo Failure.NetworkConnection
            }

            Then("Verify the called to dependencies") {
                verify(exactly = UserRepositoryImpTest.VERIFY_ONE_INTERACTION)
                { networkHandler.isConnected }
                coVerify { dataSource.add(any()) wasNot Called }
                confirmVerified(networkHandler, dataSource)
            }
        }

        Scenario("Should generate a Generic Error") {
            val error = UserRepositoryImpTest.ERROR_GENERIC
            lateinit var result: Either<Failure, List<User>>

            Given("Network is connected and data source return an Error") {
                every { networkHandler.isConnected } returns UserRepositoryImpTest.NETWORK_CONNECTED
                coEvery {
                    dataSource.getAllUsers()
                } throws Exception(error)
            }

            When("Run to get all users") {
                runBlocking {
                    result = userRepository.getAllUsers()
                }
            }

            Then("Verify called dependencies handler") {
                coVerifySequence {
                    networkHandler.isConnected
                    dataSource.getAllUsers()
                }
            }

            Then("Verify Exception") {
                ((result as Either.Left).a
                        as Failure.GenericError)
                    .exception.message shouldBeEqualTo error
            }
        }
    }

}) {

    private const val NETWORK_CONNECTED = true
    private const val NETWORK_DISCONNECTED = false
    private const val SUCCESSFUL_OPERATION = true
    private const val VERIFY_ONE_INTERACTION = 1
    private const val ERROR_GENERIC = "error_generic"

    private val users: List<UserEntity>
        get() = mutableListOf<UserEntity>().apply {
            add(createUser())
            add(createUser())
            add(createUser())
            add(createUser())
            add(createUser())
        }

    private fun createUser(): UserEntity {
        val faker = Faker()
        return UserEntity(
            faker.name.firstName(),
            faker.name.lastName(),
            faker.phoneNumber.phoneNumber(),
            faker.address.fullAddress()
        )
    }
}