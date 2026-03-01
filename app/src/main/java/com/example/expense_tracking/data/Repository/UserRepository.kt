package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateProfile(userId: String, name: String, phone: String): Boolean {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(mapOf("name" to name, "phone" to phone))
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getCurrentUserAuth(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun updateEmail(userId: String, newEmail: String): Boolean {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("email", newEmail)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




    suspend fun updatePassword(newPassword: String): Boolean {
        val user: FirebaseUser = auth.currentUser ?: return false
        return try {
            user.updatePassword(newPassword).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun logout() {
        auth.signOut()
    }
}
