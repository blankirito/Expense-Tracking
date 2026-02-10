package com.example.expense_tracking.data.Repository

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginRepository {
    var login_email by mutableStateOf("")
    var login_password by mutableStateOf("")
    var loginSuccess by mutableStateOf(false)
    var loginError by mutableStateOf("")

    private val db = Firebase.firestore

    fun login(onComplete: (Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", login_email)
            .whereEqualTo("password", login_password)
            .get()
            .addOnSuccessListener {  result ->
                if (result.isEmpty) {
                    loginError = "Email not registered"
                    loginSuccess = false
                    onComplete(false)
                } else {
                    var userDoc = result.documents[0]
                    val password = userDoc.getString("password") ?: ""
                    if (password == login_password) {
                        loginSuccess = true
                        loginError = ""
                        onComplete(true)
                    } else {
                        loginSuccess = false
                        loginError = "Wrong password"
                        onComplete(false)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Login failed", e)
                loginSuccess = false
                loginError = "Login failed"
                onComplete(false)
            }
    }
}