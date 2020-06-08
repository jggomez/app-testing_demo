package com.devhack.appdemofortests.data.repositories

interface DataSource {
    suspend fun add(user: UserEntity): Boolean
}