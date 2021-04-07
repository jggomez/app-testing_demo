package com.devhack.appdemofortests.steps

import androidx.test.rule.ActivityTestRule
import com.devhack.appdemofortests.ui.activities.MainActivity
import com.devhack.appdemofortests.ui.registerusers.robots.RegisterUserRobot
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

class RegisterUserSteps {

    private val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val registerUserRobot by lazy {
        RegisterUserRobot()
    }

    @Given("^El usuario esta en el formulario para crear una cuenta y llena los datos (nombre, apellido, celular, dirección)$")
    fun initRegisterUser() {
    }

    @When("^El usuario digita el nombre (\\\\S+)$")
    fun typeName(name: String) {
        registerUserRobot.typeName(name)
    }

    @When("^El usuario digita el apellido (\\\\S+)$")
    fun typeLastName(lastName: String) {
        registerUserRobot.typeLastName(lastName)
    }

    @When("^El usuario digita el numero de celular (\\\\S+)$")
    fun typeCellPhone(cellPhone: String) {
        registerUserRobot.typeCellPhone(cellPhone)
    }

    @When("^El usuario digita la dirección (\\\\S+)$")
    fun typeAddress(address: String) {
        registerUserRobot.typeAddress(address)
    }

    @Then("^El usuario se redirecciona a la pantalla donde están todos los usuarios$")
    fun send() {
        registerUserRobot.sleep(2000)
        registerUserRobot.send {
            isSuccessUserList()
        }
    }
}
