package com.devhack.appdemofortests.data.repositories

import com.devhack.appdemofortests.usecases.User

data class UserEntity(
    val name: String = "",
    val lastName: String = "",
    val cellPhone: String = "",
    val address: String = ""
) {
    fun toUser() =
        User(
            name,
            lastName,
            cellPhone,
            address
        )
}
