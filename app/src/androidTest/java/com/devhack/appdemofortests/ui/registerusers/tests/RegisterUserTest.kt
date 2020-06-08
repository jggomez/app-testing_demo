package com.devhack.appdemofortests.ui.registerusers.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.devhack.appdemofortests.ui.activities.MainActivity
import com.devhack.appdemofortests.ui.registerusers.robots.RegisterUserRobot
import com.devhack.appdemofortests.ui.registerusers.robots.ResultRobot
import com.devhack.appdemofortests.ui.registerusers.robots.registerUser
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RegisterUserTest {

    companion object {
        const val TIME_WAIT_MILLIS = 1000L
    }

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    /*
      Given: El usuario ve un formulario y llena los datos (nombre, apellido, celular, dirección)
      When: el usuario da clik en Enviar
      Then: El usuario va a la pantalla donde se visualiza todos los usuarios
     */

    @Test
    fun shouldSeeListUsers() {
        registerUser {
            typeName(DataTests.NAME)
            typeLastName(DataTests.LAST_NAME)
            typeCellPhone(DataTests.CELL_PHONE)
            typeAddress(DataTests.ADDRESS)
            sleep(2000)
        } send {
            isSuccessUserList()
        }
    }
}
