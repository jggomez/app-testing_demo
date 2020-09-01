package com.devhack.appdemofortests.data.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreDataSource : DataSource {

    companion object {
        const val USERS_COLLECTION = "userstest"
    }

    private val db by lazy {
        Firebase.firestore
    }

    override suspend fun add(user: UserEntity): Boolean =
        suspendCoroutine { continuation ->
            db.collection(USERS_COLLECTION)
                .add(user)
                .addOnSuccessListener { continuation.resume(true) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

    override suspend fun getAllUsers(): List<UserEntity> =
        suspendCoroutine { continuation ->
            db.collection(USERS_COLLECTION)
                .get()
                .addOnFailureListener { continuation.resumeWithException(it) }
                .addOnSuccessListener {
                    continuation.resume(it.toObjects(UserEntity::class.java))
                }
        }
}
