package com.devhack.appdemofortests.ui.registerusers.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.devhack.appdemofortests.R

fun registerUser(block: RegisterUserRobot.() -> Unit) = RegisterUserRobot().apply(block)

class RegisterUserRobot {

    companion object {
        const val SEND_TEXT = "Send"
    }

    fun typeName(name: String) {
        onView(
            withId(R.id.txtName)
        ).perform(
            typeText(name),
            closeSoftKeyboard()
        )
    }

    fun typeLastName(lastName: String) {
        onView(
            withId(R.id.txtLastName)
        ).perform(
            typeText(lastName),
            closeSoftKeyboard()
        )
    }

    fun typeCellPhone(cellPhone: String) {
        onView(
            withId(R.id.txtCellPhoneNumber)
        ).perform(
            typeText(cellPhone),
            closeSoftKeyboard()
        )
    }

    fun typeAddress(address: String) {
        onView(
            withId(R.id.txtAddress)
        ).perform(
            typeText(address),
            closeSoftKeyboard()
        )
    }

    infix fun send(block: ResultRobot.() -> Unit): ResultRobot {
        onView(withText(SEND_TEXT)).perform(click())
        return ResultRobot().apply(block)
    }

    fun sleep(millis: Long) {
        Thread.sleep(millis)
    }
}

class ResultRobot {

    fun isSuccessUserList() {
        onView(withId(R.id.rvUsers)).check(
            matches(isDisplayed())
        )
    }

}