package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.expense_tracking.data.Repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel {
    private val repository = LoginRepository()

    var isLoggedIn by mutableStateOf(false)

    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var loginError by mutableStateOf<String?>(null)


    fun loginWithEmail(onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(loginEmail, loginPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoggedIn = true
                    loginError = null
                    onComplete(true)
                } else {
                    loginError = task.exception?.message ?: "Login failed"
                    isLoggedIn = false
                    onComplete(false)
                }
            }
    }
}